package de.consol.dus.s4.services.aggregator.usecases.api.requests;

import de.consol.dus.s4.services.aggregator.usecases.AggregateUploadDataUseCase;
import de.consol.dus.s4.services.aggregator.usecases.spi.dao.requests.GetUploadByIdRequest;

public interface AggregateUploadDataRequest extends GetUploadByIdRequest {
  default void execute() {
    AggregateUploadDataUseCase.getInitializedInstance(getCorrelationId()).execute(this);
  }
}
