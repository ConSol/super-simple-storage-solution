package de.consol.dus.s4.services.rest.api.usecases;

import de.consol.dus.s4.commons.http.exceptions.NoSuchEntityException;
import de.consol.dus.s4.services.rest.api.usecases.api.exceptions.TotalPartsAlreadySetException;
import de.consol.dus.s4.services.rest.api.usecases.api.exceptions.TotalPartsSmallerThanMaxPartNumberException;
import de.consol.dus.s4.services.rest.api.usecases.api.requests.SetTotalPartsForUploadRequest;
import de.consol.dus.s4.services.rest.api.usecases.api.responses.Upload;
import de.consol.dus.s4.services.rest.api.usecases.api.responses.UploadPart;
import de.consol.dus.s4.services.rest.api.usecases.spi.dao.UploadDao;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import java.util.Objects;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;

@ApplicationScoped
@RequiredArgsConstructor
public class SetTotalPartsUseCase {
  private final UploadDao dao;
  private final Logger logger;
  private final EnterProcessingStageUseCase enterProcessingStageUseCase;

  @WithSpan
  public Upload execute(SetTotalPartsForUploadRequest request)
      throws TotalPartsAlreadySetException, TotalPartsSmallerThanMaxPartNumberException {
    logger.info("Received request: {}", request);
    final int totalPartsFromRequest = request.totalParts();
    final long id = request.id();
    final Upload fetched = dao.getById(id)
        .orElseThrow(() -> new NoSuchEntityException(Upload.class, id));
    throwIfTotalPartsAlreadySet(totalPartsFromRequest, fetched);
    throwIfTotalPartsTooSmall(request, totalPartsFromRequest, fetched);
    final Optional<? extends Upload> maybeUpdated =
        dao.setTotalPartsForUpload(request);
    enterProcessingStageUseCase.execute(fetched.getId());
    return maybeUpdated.map(Upload.class::cast)
        .orElseThrow(() -> new NoSuchEntityException(Upload.class, id));
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
          request.id(),
          maxPartNumber,
          request.totalParts());
      throw new TotalPartsSmallerThanMaxPartNumberException(
          request.id(),
          totalPartsFromRequest,
          maxPartNumber);
    }
  }
}
