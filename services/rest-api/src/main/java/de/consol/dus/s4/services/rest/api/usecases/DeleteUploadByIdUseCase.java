package de.consol.dus.s4.services.rest.api.usecases;

import de.consol.dus.s4.services.rest.api.usecases.spi.dao.UploadDao;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import javax.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;

@ApplicationScoped
@RequiredArgsConstructor
public class DeleteUploadByIdUseCase {
  private final UploadDao dao;
  private final Logger logger;

  @WithSpan
  public void execute(long id) {
    logger.info("Received request to delete upload with id {}", id);
    dao.deleteById(id);
  }
}
