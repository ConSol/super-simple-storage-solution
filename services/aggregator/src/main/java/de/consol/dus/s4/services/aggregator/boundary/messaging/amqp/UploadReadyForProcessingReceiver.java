package de.consol.dus.s4.services.aggregator.boundary.messaging.amqp;

import de.consol.dus.s4.services.aggregator.boundary.messaging.amqp.messages.UploadReadyForProcessing;
import de.consol.dus.s4.services.aggregator.boundary.messaging.amqp.requests.AggreagateUploadDataRequestImpl;
import io.smallrye.reactive.messaging.amqp.IncomingAmqpMetadata;
import io.smallrye.reactive.messaging.annotations.Blocking;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import javax.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

@ApplicationScoped
@AllArgsConstructor
public class UploadReadyForProcessingReceiver {
  @Incoming("amqp-upload-ready-for-processing-incoming")
  @Blocking
  protected CompletionStage<Void> receive(Message<UploadReadyForProcessing> message) {
    try {
      new AggreagateUploadDataRequestImpl(
          message.getPayload().getId(),
          message.getMetadata(IncomingAmqpMetadata.class)
              .map(IncomingAmqpMetadata::getCorrelationId)
              .orElse(UUID.randomUUID().toString()))
          .execute();
      return message.ack();
    } catch (Exception cause) {
      return message.nack(cause);
    }
  }
}
