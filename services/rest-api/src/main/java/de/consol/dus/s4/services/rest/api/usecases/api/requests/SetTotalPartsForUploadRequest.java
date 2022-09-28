package de.consol.dus.s4.services.rest.api.usecases.api.requests;

import de.consol.dus.s4.services.rest.api.usecases.SetTotalPartsUseCase;
import de.consol.dus.s4.services.rest.api.usecases.api.exceptions.TotalPartsAlreadySetException;
import de.consol.dus.s4.services.rest.api.usecases.api.exceptions.TotalPartsSmallerThanMaxPartNumberException;
import de.consol.dus.s4.services.rest.api.usecases.api.responses.Upload;
import java.util.Optional;
import javax.enterprise.inject.spi.CDI;

public interface SetTotalPartsForUploadRequest extends
    de.consol.dus.s4.services.rest.api.usecases.spi.dao.requests.SetTotalPartsForUploadRequest {

  default Optional<Upload> execute()
      throws TotalPartsAlreadySetException, TotalPartsSmallerThanMaxPartNumberException {
    return CDI.current().select(SetTotalPartsUseCase.class).get().execute(this);
  }
}
