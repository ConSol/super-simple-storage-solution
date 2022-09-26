package de.consol.dus.s4.services.rest.api.usecases.spi.dao;

import de.consol.dus.s4.services.rest.api.usecases.api.exceptions.PartNumberAlreadyExistsException;
import de.consol.dus.s4.services.rest.api.usecases.api.exceptions.PartNumberIsBiggerThanTotalParts;
import de.consol.dus.s4.services.rest.api.usecases.spi.dao.requests.AddPartToUploadRequest;
import de.consol.dus.s4.services.rest.api.usecases.spi.dao.requests.CreateNewUploadRequest;
import de.consol.dus.s4.services.rest.api.usecases.spi.dao.requests.SetStatusOfUploadRequest;
import de.consol.dus.s4.services.rest.api.usecases.spi.dao.requests.SetTotalPartsForUploadRequest;
import de.consol.dus.s4.services.rest.api.usecases.spi.dao.responses.Upload;
import java.util.Collection;
import java.util.Optional;

public interface UploadDao {
  Upload createNewUpload(CreateNewUploadRequest request);

  Optional<Upload> getById(long id);

  void deleteById(long id);

  Optional<Upload> addPartToUpload(AddPartToUploadRequest request)
      throws PartNumberAlreadyExistsException, PartNumberIsBiggerThanTotalParts;

  Collection<Upload> getAll();

  Optional<Upload> setTotalPartsForUpload(SetTotalPartsForUploadRequest request);

  void setStatusOfUpload(SetStatusOfUploadRequest request);
}
