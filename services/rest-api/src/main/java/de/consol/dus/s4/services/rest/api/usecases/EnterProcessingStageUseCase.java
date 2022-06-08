package de.consol.dus.s4.services.rest.api.usecases;

import de.consol.dus.s4.services.rest.api.usecases.api.exceptions.SingletonNotInitializedError;
import de.consol.dus.s4.services.rest.api.usecases.api.responses.Upload;
import de.consol.dus.s4.services.rest.api.usecases.api.responses.UploadStatus;
import de.consol.dus.s4.services.rest.api.usecases.internal.api.EnterProcessingRequest;
import de.consol.dus.s4.services.rest.api.usecases.spi.dao.UploadDao;
import de.consol.dus.s4.services.rest.api.usecases.spi.dao.requests.SetStatusOfUploadRequest;
import de.consol.dus.s4.services.rest.api.usecases.spi.messaging.UploadReadyForProcessingEmitter;
import de.consol.dus.s4.services.rest.api.usecases.spi.messaging.requests.SendUploadReadyForProcessingRequest;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;

@RequiredArgsConstructor
public class EnterProcessingStageUseCase {
  private static final AtomicReference<EnterProcessingStageUseCase> INSTANCE =
      new AtomicReference<>();

  private final UploadDao dao;
  private final Logger logger;
  private final UploadReadyForProcessingEmitter emitter;

  public void execute(EnterProcessingRequest request) {
    try {
      logger.info("Received request: {}", request);
      final long id = request.getId();
      if (updateCanBeProcessed(request)) {
        logger.debug("Request: {}: Upload with id {} is ready for processing", request, id);
        setStatusToProcessing(request);
        emitter.emit(new SendUploadReadyForProcessingRequest(id, request.getCorrelationId()));
      }
    } catch (Exception e) {
      logger.error("Request {}: error occurred", request, e);
    }
  }

  private boolean updateCanBeProcessed(EnterProcessingRequest request) {
    final long id = request.getId();
    final Optional<? extends Upload> maybeUpload = dao.getById(request);
    if (maybeUpload.isEmpty()) {
      logger.debug("Request: {}: Upload with id {} odes not exist", request, id);
      return false;
    }
    final Upload upload = maybeUpload.get();
    if (isAlreadyInProcessingState(upload, request)) {
      return false;
    }
    if (isAlreadyDone(upload, request)) {
      return false;
    }
    if (hasNoTotalSize(upload, request)) {
      return false;
    }
    return allPartsReceived(upload, request);
  }

  private boolean isAlreadyInProcessingState(Upload upload, EnterProcessingRequest request) {
    if (Objects.equals(upload.getStatus(), UploadStatus.PROCESSING)) {
      logger.debug(
          "Request: {}: Upload with id {} is already in processing state",
          request,
          request.getId());
      return true;
    }
    return false;
  }

  private boolean isAlreadyDone(Upload upload, EnterProcessingRequest request) {
    if (Objects.equals(upload.getStatus(), UploadStatus.DONE)) {
      logger.debug("Request: {}: Upload with id {} is already done", request, request.getId());
      return true;
    }
    return false;
  }

  private boolean hasNoTotalSize(Upload upload, EnterProcessingRequest request) {
    if (Objects.isNull(upload.getTotalParts())) {
      logger.debug(
          "Request: {}: Upload with id {} has totalParts not yet set",
          request,
          request.getId());
      return true;
    }
    return false;
  }

  private boolean allPartsReceived(Upload upload, EnterProcessingRequest request) {
    final int receivedParts = upload.getParts().size();
    final int totalParts = upload.getTotalParts();
    if (receivedParts != totalParts) {
      logger.debug(
          "Request: {}: Upload with id {} has totalParts set to {}, but only {} received until now",
          request,
          request.getId(),
          totalParts,
          receivedParts);
      return false;
    }
    return true;
  }

  private void setStatusToProcessing(EnterProcessingRequest request) {
    dao.setStatusOfUpload(new SetStatusOfUploadRequest(
        request.getId(),
        UploadStatus.PROCESSING,
        request.getCorrelationId()));
  }

  public static EnterProcessingStageUseCase getInitializedInstance(String correlationId) {
    try {
      return getInstance(null, null, null);
    } catch (IllegalStateException cause) {
      throw new SingletonNotInitializedError(cause, correlationId);
    }
  }

  public static EnterProcessingStageUseCase getInstance(
      UploadDao dao,
      Logger logger,
      UploadReadyForProcessingEmitter emitter) {
    if (Objects.isNull(INSTANCE.get())) {
      synchronized (EnterProcessingStageUseCase.class) {
        if (Objects.isNull(INSTANCE.get())) {
          if (Objects.isNull(dao) || Objects.isNull(logger) || Objects.isNull(emitter)) {
            throw new IllegalStateException(
                "nulls are only allowed when the singleton is initialized. The singleton is not "
                    + " yet initialized. Please call static method "
                    + "EnterProcessingStageUseCase.getInstance(UploadDao, Logger) with non-null "
                    + "parameters.");
          }
          INSTANCE.set(new EnterProcessingStageUseCase(dao, logger, emitter));
        }
      }
    }
    return INSTANCE.get();
  }
}
