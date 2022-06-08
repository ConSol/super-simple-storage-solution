package de.consol.dus.s4.services.rest.api.usecases.internal.api;

import de.consol.dus.s4.commons.correlation.Correlated;
import de.consol.dus.s4.services.rest.api.usecases.EnterProcessingStageUseCase;
import de.consol.dus.s4.services.rest.api.usecases.spi.dao.requests.GetUploadByIdRequest;
import io.smallrye.mutiny.Uni;
import lombok.Value;

@Value
public class EnterProcessingRequest implements GetUploadByIdRequest, Correlated {
  long id;
  String correlationId;

  public <T> T execute(T t) {
    execute();
    return t;
  }

  private void execute() {
    Uni.createFrom()
        .item(EnterProcessingStageUseCase.getInitializedInstance(getCorrelationId()))
        .onItem()
            .invoke(useCase -> useCase.execute(this))
        .subscribe().asCompletionStage();
  }


}
