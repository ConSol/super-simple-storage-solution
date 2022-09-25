package de.consol.dus.s4.services.rest.api.usecases.api.requests;

import de.consol.dus.s4.services.rest.api.usecases.SetTotalPartsUseCase;
import de.consol.dus.s4.services.rest.api.usecases.api.exceptions.TotalPartsAlreadySetException;
import de.consol.dus.s4.services.rest.api.usecases.api.exceptions.TotalPartsSmallerThanMaxPartNumberException;
import de.consol.dus.s4.services.rest.api.usecases.api.responses.Upload;
import de.consol.dus.s4.services.rest.api.usecases.spi.dao.requests.GetUploadByIdRequest;
import java.util.Optional;

public interface SetTotalPartsForUploadRequest extends GetUploadByIdRequest,
    de.consol.dus.s4.services.rest.api.usecases.spi.dao.requests.SetTotalPartsForUploadRequest {


  default Optional<Upload> execute()
      throws TotalPartsAlreadySetException, TotalPartsSmallerThanMaxPartNumberException {
    return SetTotalPartsUseCase.getInitializedInstance().execute(this);
  }
}
