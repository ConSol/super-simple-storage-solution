package de.consol.dus.s4.services.aggregator.boundary.messaging.amqp.requests;

import de.consol.dus.s4.services.aggregator.usecases.api.requests.AggregateUploadDataRequest;
import lombok.ToString;
import lombok.Value;

@Value
@ToString(exclude = "correlationId")
public class AggreagateUploadDataRequestImpl implements AggregateUploadDataRequest {
  long id;
  String correlationId;
}
