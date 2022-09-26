package de.consol.dus.s4.services.rest.api.usecases.spi.dao.requests;

import de.consol.dus.s4.services.rest.api.usecases.api.responses.Upload;

public interface CreateNewUploadRequest {
  String getFileName();

  Upload execute();
}
