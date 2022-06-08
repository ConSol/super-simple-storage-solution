package de.consol.dus.s4.services.aggregator.boundary.messaging.amqp.messages;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
@Builder
public class UploadReadyForProcessing {
  long id;
}
