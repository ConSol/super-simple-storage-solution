package de.consol.dus.s4.services.rest.api.usecases;

import de.consol.dus.s4.services.rest.api.usecases.api.requests.CreateNewUploadRequest;
import de.consol.dus.s4.services.rest.api.usecases.api.responses.Upload;
import de.consol.dus.s4.services.rest.api.usecases.spi.dao.UploadDao;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import javax.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;

@ApplicationScoped
@RequiredArgsConstructor
public class CreateNewUploadUseCase {
  private final UploadDao dao;
  private final Logger logger;

  @WithSpan
  public Upload execute(CreateNewUploadRequest request) {
    logger.info("Received request: {}", request);
    return dao.createNewUpload(request);
  }
}
