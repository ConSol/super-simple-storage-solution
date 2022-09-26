package de.consol.dus.s4.services.rest.api.usecases.internal.api;

import de.consol.dus.s4.services.rest.api.usecases.EnterProcessingStageUseCase;
import io.smallrye.mutiny.Uni;
import lombok.Value;

@Value
public class EnterProcessingRequest {
  long id;

  public <T> T execute(T t) {
    execute();
    return t;
  }

  private void execute() {
    Uni.createFrom()
        .item(EnterProcessingStageUseCase.getInitializedInstance())
        .onItem()
            .invoke(useCase -> useCase.execute(getId()))
        .subscribe().asCompletionStage();
  }
}