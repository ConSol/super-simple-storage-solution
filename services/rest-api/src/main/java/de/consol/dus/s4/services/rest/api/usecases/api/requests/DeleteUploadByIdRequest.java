package de.consol.dus.s4.services.rest.api.usecases.api.requests;

import de.consol.dus.s4.services.rest.api.usecases.DeleteUploadByIdUseCase;

public interface DeleteUploadByIdRequest
    extends de.consol.dus.s4.services.rest.api.usecases.spi.dao.requests.DeleteUploadByIdRequest {

  default void execute() {
    DeleteUploadByIdUseCase.getInitializedInstance().execute(this);
  }
}