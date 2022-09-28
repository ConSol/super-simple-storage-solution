package de.consol.dus.s4.services.rest.api.usecases.api.requests;

import de.consol.dus.s4.services.rest.api.usecases.CreateNewUploadUseCase;
import de.consol.dus.s4.services.rest.api.usecases.api.responses.Upload;
import javax.enterprise.inject.spi.CDI;

public interface CreateNewUploadRequest extends
    de.consol.dus.s4.services.rest.api.usecases.spi.dao.requests.CreateNewUploadRequest {

  default Upload execute() {
    return CDI.current().select(CreateNewUploadUseCase.class).get().execute(this);
  }
}
