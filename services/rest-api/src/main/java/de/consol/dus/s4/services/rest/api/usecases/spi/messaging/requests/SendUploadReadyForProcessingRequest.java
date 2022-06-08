package de.consol.dus.s4.services.rest.api.usecases.spi.messaging.requests;

import de.consol.dus.s4.commons.correlation.Correlated;
import lombok.Value;

@Value
public class SendUploadReadyForProcessingRequest implements Correlated {
  long id;
  String correlationId;
}
