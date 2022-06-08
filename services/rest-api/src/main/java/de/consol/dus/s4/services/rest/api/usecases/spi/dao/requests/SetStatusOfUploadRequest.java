package de.consol.dus.s4.services.rest.api.usecases.spi.dao.requests;

import de.consol.dus.s4.commons.correlation.Correlated;
import de.consol.dus.s4.services.rest.api.usecases.api.responses.UploadStatus;
import lombok.Value;

@Value
public class SetStatusOfUploadRequest implements Correlated {
  long id;
  UploadStatus status;
  String correlationId;
}
