package de.consol.dus.s4.services.rest.api.usecases;

import de.consol.dus.s4.services.rest.api.usecases.api.exceptions.SingletonNotInitializedError;
import de.consol.dus.s4.services.rest.api.usecases.api.exceptions.TotalPartsAlreadySetException;
import de.consol.dus.s4.services.rest.api.usecases.api.exceptions.TotalPartsSmallerThanMaxPartNumberException;
import de.consol.dus.s4.services.rest.api.usecases.api.requests.SetTotalPartsForUploadRequest;
import de.consol.dus.s4.services.rest.api.usecases.api.responses.Upload;
import de.consol.dus.s4.services.rest.api.usecases.api.responses.UploadPart;
import de.consol.dus.s4.services.rest.api.usecases.internal.api.EnterProcessingRequest;
import de.consol.dus.s4.services.rest.api.usecases.spi.dao.UploadDao;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SetTotalPartsUseCase {
  private static final AtomicReference<SetTotalPartsUseCase> INSTANCE = new AtomicReference<>();

  private final UploadDao dao;
  private final Logger logger;

  public Optional<Upload> execute(SetTotalPartsForUploadRequest request)
      throws TotalPartsAlreadySetException, TotalPartsSmallerThanMaxPartNumberException {
    logger.info("Received request: {}", request);
    final int totalPartsFromRequest = request.getTotalParts();
    final Optional<? extends Upload> maybeUpload = dao.getById(request.getId());
    if (maybeUpload.isEmpty()) {
      return Optional.empty();
    }
    final Upload fetched = maybeUpload.get();
    throwIfTotalPartsAlreadySet(totalPartsFromRequest, fetched);
    throwIfTotalPartsTooSmall(request, totalPartsFromRequest, fetched);
    return dao.setTotalPartsForUpload(request)
        .map(new EnterProcessingRequest(request.getId())::execute)
        .map(Upload.class::cast);
  }

  private void throwIfTotalPartsAlreadySet(int totalPartsFromRequest, Upload upload)
      throws TotalPartsAlreadySetException {
    if (Objects.nonNull(upload.getTotalParts())) {
      logger.info(
          "totalParts for Upload with id {} already set (requested: {}, actual: {})",
          upload.getId(),
          upload.getTotalParts(),
          totalPartsFromRequest);
      throw new TotalPartsAlreadySetException(
          upload.getId(),
          upload.getTotalParts());
    }
  }

  private void throwIfTotalPartsTooSmall(
      SetTotalPartsForUploadRequest request,
      int totalPartsFromRequest,
      Upload upload) throws TotalPartsSmallerThanMaxPartNumberException {
    final int maxPartNumber = upload.getParts().stream()
        .map(UploadPart::getPartNumber)
        .mapToInt(Integer::intValue)
        .max()
        .orElse(0);
    if (totalPartsFromRequest < maxPartNumber) {
      logger.info(
          "Upload with id {} already has a part with partNumber {}, hence totalParts cannot be set "
              + "to {}",
          request.getId(),
          maxPartNumber,
          request.getTotalParts());
      throw new TotalPartsSmallerThanMaxPartNumberException(
          request.getId(),
          totalPartsFromRequest,
          maxPartNumber);
    }
  }

  public static SetTotalPartsUseCase getInitializedInstance() {
    try {
      return getInstance(null, null);
    } catch (IllegalStateException cause) {
      throw new SingletonNotInitializedError(cause);
    }
  }

  public static SetTotalPartsUseCase getInstance(UploadDao dao, Logger logger) {
    if (Objects.isNull(INSTANCE.get())) {
      synchronized (SetTotalPartsUseCase.class) {
        if (Objects.isNull(INSTANCE.get())) {
          if (Objects.isNull(dao) || Objects.isNull(logger)) {
            throw new IllegalStateException(
                "nulls are only allowed when the singleton is initialized. The singleton is not "
                    + " yet initialized. Please call static method "
                    + "SetTotalPartsUseCase.getInstance(UploadDao, Logger) with non-null "
                    + "parameters.");
          }
          INSTANCE.set(new SetTotalPartsUseCase(dao, logger));
        }
      }
    }
    return INSTANCE.get();
  }
}
