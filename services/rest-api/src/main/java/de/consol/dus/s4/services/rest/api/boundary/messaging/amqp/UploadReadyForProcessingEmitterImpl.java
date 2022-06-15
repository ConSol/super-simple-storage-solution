package de.consol.dus.s4.services.rest.api.boundary.messaging.amqp;

import de.consol.dus.s4.services.rest.api.boundary.messaging.amqp.messages.UploadReadyForProcessing;
import de.consol.dus.s4.services.rest.api.usecases.spi.messaging.UploadReadyForProcessingEmitter;
import de.consol.dus.s4.services.rest.api.usecases.spi.messaging.requests.SendUploadReadyForProcessingRequest;
import io.smallrye.reactive.messaging.amqp.OutgoingAmqpMetadata;
import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;

@ApplicationScoped
public class UploadReadyForProcessingEmitterImpl
    implements UploadReadyForProcessingEmitter {

  private final Emitter<UploadReadyForProcessing> emitter;

  public UploadReadyForProcessingEmitterImpl(
      @Channel("amqp-upload-ready-for-processing-outgoing")
      Emitter<UploadReadyForProcessing> emitter) {
    this.emitter = emitter;
  }


  public void emit(SendUploadReadyForProcessingRequest request) {
    emitter.send(Message.of(new UploadReadyForProcessing(request.getId()))
        .addMetadata(OutgoingAmqpMetadata.builder()
            .withCorrelationId(request.getCorrelationId())
            .build()));
  }
}
