package de.consol.dus.s4.services.rest.api.usecases.spi.messaging.requests;

import lombok.Value;

@Value
public class SendUploadReadyForProcessingRequest {
  long id;
}
