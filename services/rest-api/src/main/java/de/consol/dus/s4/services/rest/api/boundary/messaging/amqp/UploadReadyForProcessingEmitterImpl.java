package de.consol.dus.s4.services.rest.api.boundary.messaging.amqp;

import de.consol.dus.s4.commons.opentracing.messaging.amqp.TracedAmqpEmitter;
import de.consol.dus.s4.services.rest.api.boundary.messaging.amqp.messages.UploadReadyForProcessing;
import de.consol.dus.s4.services.rest.api.usecases.spi.messaging.UploadReadyForProcessingEmitter;
import de.consol.dus.s4.services.rest.api.usecases.spi.messaging.requests.SendUploadReadyForProcessingRequest;
import io.opentracing.Tracer;
import io.smallrye.reactive.messaging.amqp.OutgoingAmqpMetadata;
import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;

@ApplicationScoped
public class UploadReadyForProcessingEmitterImpl extends TracedAmqpEmitter<UploadReadyForProcessing>
    implements UploadReadyForProcessingEmitter {
  public static final String DESTINATION = "aggregator";

  public UploadReadyForProcessingEmitterImpl(
      Tracer tracer,
      @Channel(DESTINATION) Emitter<UploadReadyForProcessing> emitter,
      Logger logger) {
    super(tracer, emitter, logger);
  }

  @Override
  public void emit(SendUploadReadyForProcessingRequest request) {
    emit(
        Message.of(new UploadReadyForProcessing(request.getId())),
        OutgoingAmqpMetadata.builder()
            .withCorrelationId(request.getCorrelationId()));
  }

  @Override
  protected String getDestinationName() {
    return DESTINATION;
  }
}
