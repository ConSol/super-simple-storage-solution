package de.consol.dus.s4.services.aggregator.usecases.spi.dao;

import de.consol.dus.s4.services.aggregator.usecases.spi.dao.requests.GetUploadByIdRequest;
import de.consol.dus.s4.services.aggregator.usecases.spi.dao.requests.WriteContentAndReleasePartsRequest;
import de.consol.dus.s4.services.aggregator.usecases.spi.dao.responses.Upload;
import java.util.Optional;

public interface UploadDao {
  Optional<Upload> getById(GetUploadByIdRequest request);

  void writeContentAndDeletePartsAndSetStatusToDone(WriteContentAndReleasePartsRequest request);
}
