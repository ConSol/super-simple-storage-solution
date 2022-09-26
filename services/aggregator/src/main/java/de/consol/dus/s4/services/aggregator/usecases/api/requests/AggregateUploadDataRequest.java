package de.consol.dus.s4.services.aggregator.usecases.api.requests;

import de.consol.dus.s4.services.aggregator.usecases.AggregateUploadDataUseCase;

public interface AggregateUploadDataRequest {
  long getId();

  default void execute() {
    AggregateUploadDataUseCase.getInitializedInstance().execute(getId());
  }
}
