package de.consol.dus.s4.services.rest.api.usecases;

import de.consol.dus.s4.services.rest.api.usecases.api.responses.Upload;
import de.consol.dus.s4.services.rest.api.usecases.spi.dao.UploadDao;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import java.util.Collection;
import javax.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;

@ApplicationScoped
@RequiredArgsConstructor
public class GetAllUploadsUseCase {
  private final UploadDao dao;
  private final Logger logger;

  @WithSpan
  public Collection<Upload> execute() {
    logger.info("Received request to get all uploads");
    return dao.getAll().stream()
        .map(Upload.class::cast)
        .toList();
  }
}
