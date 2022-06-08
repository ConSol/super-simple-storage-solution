package de.consol.dus.s4.services.rest.api.usecases.api.requests;

import de.consol.dus.s4.services.rest.api.usecases.GetUploadByIdUseCase;
import de.consol.dus.s4.services.rest.api.usecases.api.responses.Upload;
import java.util.Optional;

public interface GetUploadByIdRequest extends
    de.consol.dus.s4.services.rest.api.usecases.spi.dao.requests.GetUploadByIdRequest {

  default Optional<Upload> execute() {
    return GetUploadByIdUseCase.getInitializedInstance(getCorrelationId()).execute(this);
  }
}
