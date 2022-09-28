package de.consol.dus.s4.services.aggregator.usecases.api.requests;

import de.consol.dus.s4.services.aggregator.usecases.AggregateUploadDataUseCase;
import javax.enterprise.inject.spi.CDI;

public interface AggregateUploadDataRequest {
  long getId();

  default void execute() {
    CDI.current().select(AggregateUploadDataUseCase.class).get().execute(getId());
  }
}
