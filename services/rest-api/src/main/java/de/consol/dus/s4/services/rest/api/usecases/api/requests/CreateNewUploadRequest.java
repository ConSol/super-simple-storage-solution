package de.consol.dus.s4.services.rest.api.usecases.api.requests;

import de.consol.dus.s4.services.rest.api.usecases.CreateNewUploadUseCase;
import de.consol.dus.s4.services.rest.api.usecases.api.responses.Upload;

public interface CreateNewUploadRequest extends
    de.consol.dus.s4.services.rest.api.usecases.spi.dao.requests.CreateNewUploadRequest {

  default Upload execute() {
    return CreateNewUploadUseCase.getInitializedInstance().execute(this);
  }
}
