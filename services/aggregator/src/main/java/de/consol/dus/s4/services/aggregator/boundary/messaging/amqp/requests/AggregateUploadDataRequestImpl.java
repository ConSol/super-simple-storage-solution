package de.consol.dus.s4.services.aggregator.boundary.messaging.amqp.requests;

import de.consol.dus.s4.services.aggregator.usecases.api.requests.AggregateUploadDataRequest;
import lombok.Value;

@Value
public class AggregateUploadDataRequestImpl implements AggregateUploadDataRequest {
  long id;
}
