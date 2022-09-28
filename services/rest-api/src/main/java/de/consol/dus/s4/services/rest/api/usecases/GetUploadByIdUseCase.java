package de.consol.dus.s4.services.rest.api.usecases;

import de.consol.dus.s4.services.rest.api.usecases.api.responses.Upload;
import de.consol.dus.s4.services.rest.api.usecases.spi.dao.UploadDao;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;

@ApplicationScoped
@RequiredArgsConstructor
public class GetUploadByIdUseCase {
  private final UploadDao dao;
  private final Logger logger;

  @WithSpan
  public Optional<Upload> execute(long id) {
    logger.info("Received request to get upload with id{}", id);
    return dao.getById(id).map(Upload.class::cast);
  }
}
