package de.consol.dus.s4.services.rest.api.usecases.spi.dao.requests;

import de.consol.dus.s4.commons.correlation.Correlated;
import de.consol.dus.s4.services.rest.api.usecases.api.responses.Upload;
import de.consol.dus.s4.services.rest.api.usecases.api.responses.UploadStatus;

public interface CreateNewUploadRequest extends Correlated {
  UploadStatus INITIAL_STATUS = UploadStatus.UPLOAD_IN_PROGRESS;

  String getFileName();

  String getCorrelationId();

  Upload execute();
}
