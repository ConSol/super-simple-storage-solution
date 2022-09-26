package de.consol.dus.s4.services.rest.api.usecases.api.requests;

import de.consol.dus.s4.services.rest.api.usecases.DeleteUploadByIdUseCase;

public interface DeleteUploadByIdRequest {
  long getId();

  default void execute() {
    DeleteUploadByIdUseCase.getInitializedInstance().execute(getId());
  }
}