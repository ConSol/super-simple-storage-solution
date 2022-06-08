package de.consol.dus.s4.services.aggregator.boundary.messaging.amqp;

import de.consol.dus.s4.commons.opentracing.messaging.amqp.TracedAmqpReceiver;
import de.consol.dus.s4.services.aggregator.boundary.messaging.amqp.messages.UploadReadyForProcessing;
import de.consol.dus.s4.services.aggregator.boundary.messaging.amqp.requests.AggreagateUploadDataRequestImpl;
import io.opentracing.Tracer;
import io.smallrye.reactive.messaging.amqp.IncomingAmqpMetadata;
import io.smallrye.reactive.messaging.annotations.Blocking;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.opentracing.Traced;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;

@ApplicationScoped
public class UploadReadyForProcessingReceiver extends TracedAmqpReceiver<UploadReadyForProcessing> {

  protected static final String SOURCE = "amqp-upload-ready-for-processing-incoming";

  public UploadReadyForProcessingReceiver(Tracer tracer, Logger logger) {
    super(tracer, logger);
  }

  @Incoming(SOURCE)
  @Blocking
  protected CompletionStage<Void> receive(Message<UploadReadyForProcessing> message) {
    return this.createSpanAndCallCallback(message);
  }

  @Traced
  @Override
  protected void callback(Message<UploadReadyForProcessing> message) {
    new AggreagateUploadDataRequestImpl(
        message.getPayload().getId(),
        message.getMetadata(IncomingAmqpMetadata.class)
            .map(IncomingAmqpMetadata::getCorrelationId)
            .orElse(UUID.randomUUID().toString()))
        .execute();
  }

  @Override
  protected String getSourceName() {
    return SOURCE;
  }
}
