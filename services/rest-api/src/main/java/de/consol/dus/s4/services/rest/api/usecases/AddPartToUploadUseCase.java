package de.consol.dus.s4.services.rest.api.usecases;

import de.consol.dus.s4.services.rest.api.usecases.api.exceptions.PartNumberAlreadyExistsException;
import de.consol.dus.s4.services.rest.api.usecases.api.exceptions.PartNumberIsBiggerThanTotalParts;
import de.consol.dus.s4.services.rest.api.usecases.api.requests.AddPartToUploadRequest;
import de.consol.dus.s4.services.rest.api.usecases.api.responses.Upload;
import de.consol.dus.s4.services.rest.api.usecases.spi.dao.UploadDao;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;

@ApplicationScoped
@RequiredArgsConstructor
public final class AddPartToUploadUseCase {
  private final UploadDao dao;
  private final Logger logger;
  private final EnterProcessingStageUseCase enterProcessingStageUseCase;

  @WithSpan
  public Optional<Upload> execute(AddPartToUploadRequest request)
      throws PartNumberAlreadyExistsException, PartNumberIsBiggerThanTotalParts {
    logger.info("Received request");
    final Optional<? extends Upload> maybeUpload = dao.addPartToUpload(request);
    maybeUpload
        .map(Upload::getId)
        .ifPresent(enterProcessingStageUseCase::execute);
    return maybeUpload
        .map(Upload.class::cast);
  }
}
