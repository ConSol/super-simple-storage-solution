package de.consol.dus.s4.services.rest.api.usecases;

import de.consol.dus.s4.services.rest.api.usecases.api.exceptions.SingletonNotInitializedError;
import de.consol.dus.s4.services.rest.api.usecases.api.responses.Upload;
import de.consol.dus.s4.services.rest.api.usecases.api.responses.UploadStatus;
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

  public void execute(long id) {
    try {
      logger.info("Received request to check if Upload with id {} can be processed", id);
      if (updateCanBeProcessed(id)) {
        logger.debug("Upload with id {} is ready for processing", id);
        setStatusToProcessing(id);
        emitter.emit(new SendUploadReadyForProcessingRequest(id));
      }
    } catch (Exception e) {
      logger.error("Exception occurred", e);
    }
  }

  private boolean updateCanBeProcessed(long id) {
    final Optional<? extends Upload> maybeUpload = dao.getById(id);
    if (maybeUpload.isEmpty()) {
      logger.debug("Upload with id {} odes not exist", id);
      return false;
    }
    final Upload upload = maybeUpload.get();
    if (isAlreadyInProcessingState(upload)) {
      return false;
    }
    if (isAlreadyDone(upload)) {
      return false;
    }
    if (hasNoTotalSize(upload)) {
      return false;
    }
    return allPartsReceived(upload);
  }

  private boolean isAlreadyInProcessingState(Upload upload) {
    if (Objects.equals(upload.getStatus(), UploadStatus.PROCESSING)) {
      logger.debug("Upload with id {} is already in processing state", upload.getId());
      return true;
    }
    return false;
  }

  private boolean isAlreadyDone(Upload upload) {
    if (Objects.equals(upload.getStatus(), UploadStatus.DONE)) {
      logger.debug("Upload with id {} is already done", upload.getId());
      return true;
    }
    return false;
  }

  private boolean hasNoTotalSize(Upload upload) {
    if (Objects.isNull(upload.getTotalParts())) {
      logger.debug("Upload with id {} has totalParts not yet set", upload.getId());
      return true;
    }
    return false;
  }

  private boolean allPartsReceived(Upload upload) {
    final int receivedParts = upload.getParts().size();
    final int totalParts = upload.getTotalParts();
    if (receivedParts != totalParts) {
      logger.debug(
          "Upload with id {} has totalParts set to {}, but only {} received until now",
          upload.getId(),
          totalParts,
          receivedParts);
      return false;
    }
    return true;
  }

  private void setStatusToProcessing(long id) {
    dao.setStatusOfUpload(new SetStatusOfUploadRequest(id, UploadStatus.PROCESSING));
  }

  public static EnterProcessingStageUseCase getInitializedInstance() {
    try {
      return getInstance(null, null, null);
    } catch (IllegalStateException cause) {
      throw new SingletonNotInitializedError(cause);
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
