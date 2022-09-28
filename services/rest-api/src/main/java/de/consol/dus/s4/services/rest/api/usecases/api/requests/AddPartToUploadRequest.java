package de.consol.dus.s4.services.rest.api.usecases.api.requests;

import de.consol.dus.s4.services.rest.api.usecases.AddPartToUploadUseCase;
import de.consol.dus.s4.services.rest.api.usecases.api.exceptions.PartNumberAlreadyExistsException;
import de.consol.dus.s4.services.rest.api.usecases.api.exceptions.PartNumberIsBiggerThanTotalParts;
import de.consol.dus.s4.services.rest.api.usecases.api.responses.Upload;
import java.util.Optional;
import javax.enterprise.inject.spi.CDI;

public interface AddPartToUploadRequest extends
    de.consol.dus.s4.services.rest.api.usecases.spi.dao.requests.AddPartToUploadRequest {

  default Optional<Upload> execute()
      throws PartNumberAlreadyExistsException, PartNumberIsBiggerThanTotalParts {
    return CDI.current().select(AddPartToUploadUseCase.class).get().execute(this);
  }
}
