package de.consol.dus.s4.services.rest.api.boundary.messaging.amqp.messages;

import lombok.Value;

@Value
public class UploadReadyForProcessing {
  long id;
}
