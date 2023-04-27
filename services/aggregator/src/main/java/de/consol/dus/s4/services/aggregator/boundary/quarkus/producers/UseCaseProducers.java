package de.consol.dus.s4.services.aggregator.boundary.quarkus.producers;

import de.consol.dus.s4.services.aggregator.usecases.AggregateUploadDataUseCase;
import de.consol.dus.s4.services.aggregator.usecases.spi.dao.UploadDao;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@RequiredArgsConstructor
public class UseCaseProducers {
  private final UploadDao uploadDao;

  @Startup
  @Produces
  public AggregateUploadDataUseCase enterProcessingStageUseCase() {
    return AggregateUploadDataUseCase
        .getInstance(uploadDao, LoggerFactory.getLogger(AggregateUploadDataUseCase.class));
  }
}
