package de.consol.dus.s4.services.aggregator.boundary.quarkus.producers;

import de.consol.dus.s4.commons.correlation.logging.logger.CorrelationLogger;
import de.consol.dus.s4.services.aggregator.usecases.AggregateUploadDataUseCase;
import de.consol.dus.s4.services.aggregator.usecases.spi.dao.UploadDao;
import io.quarkus.runtime.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class UseCaseProducers {
  private final UploadDao uploadDao;

  @Startup
  @Produces
  public AggregateUploadDataUseCase enterProcessingStageUseCase() {
    return AggregateUploadDataUseCase
        .getInstance(uploadDao, new CorrelationLogger(AggregateUploadDataUseCase.class));
  }
}
