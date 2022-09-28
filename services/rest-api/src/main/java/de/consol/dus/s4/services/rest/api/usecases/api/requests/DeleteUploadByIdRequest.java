package de.consol.dus.s4.services.rest.api.usecases.api.requests;

import de.consol.dus.s4.services.rest.api.usecases.DeleteUploadByIdUseCase;
import javax.enterprise.inject.spi.CDI;

public interface DeleteUploadByIdRequest {
  long getId();

  default void execute() {
    CDI.current().select(DeleteUploadByIdUseCase.class).get().execute(getId());
  }
}