package de.consol.dus.s4.services.rest.api.usecases.spi.dao.requests;

import de.consol.dus.s4.services.rest.api.usecases.api.responses.UploadStatus;
import lombok.Value;

@Value
public class SetStatusOfUploadRequest {
  long id;
  UploadStatus status;
}
