package de.consol.dus.s4.services.rest.api.usecases.spi.dao.requests;

import de.consol.dus.s4.services.rest.api.usecases.api.responses.Upload;
import de.consol.dus.s4.services.rest.api.usecases.api.responses.UploadStatus;

public interface CreateNewUploadRequest {
  UploadStatus INITIAL_STATUS = UploadStatus.UPLOAD_IN_PROGRESS;

  String getFileName();

  Upload execute();
}
