package de.consol.dus.s4.services.rest.api.usecases.api.requests;

import de.consol.dus.s4.services.rest.api.usecases.GetAllUploadsUseCase;
import de.consol.dus.s4.services.rest.api.usecases.api.responses.Upload;
import java.util.Collection;

public interface GetAllUploadsRequest {
  default Collection<Upload> execute() {
    return GetAllUploadsUseCase.getInitializedInstance().execute();
  }
}
