package de.consol.dus.s4.services.rest.api.boundary.messaging.amqp;

import de.consol.dus.s4.commons.correlation.http.exceptions.filter.container.RequestFilter;
import de.consol.dus.s4.commons.opentelemetry.messaging.amqp.TracedAmqpEmitter;
import de.consol.dus.s4.services.rest.api.boundary.messaging.amqp.messages.UploadReadyForProcessing;
import de.consol.dus.s4.services.rest.api.usecases.spi.messaging.UploadReadyForProcessingEmitter;
import de.consol.dus.s4.services.rest.api.usecases.spi.messaging.requests.SendUploadReadyForProcessingRequest;
import io.smallrye.reactive.messaging.amqp.OutgoingAmqpMetadata;
import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.MDC;
import org.slf4j.Logger;

@ApplicationScoped
public class UploadReadyForProcessingEmitterImpl extends TracedAmqpEmitter<UploadReadyForProcessing>
    implements UploadReadyForProcessingEmitter {
  public UploadReadyForProcessingEmitterImpl(
      @Channel("amqp-upload-ready-for-processing-outgoing")
      Emitter<UploadReadyForProcessing> emitter,

      Logger logger) {
    super(emitter, logger);
  }

  @Override
  public void emit(SendUploadReadyForProcessingRequest request) {
    emit(Message.of(new UploadReadyForProcessing(request.getId()))
        .addMetadata(OutgoingAmqpMetadata.builder()
            .withCorrelationId(MDC.get(RequestFilter.CORRELATION_ID_MDC_KEY).toString())
            .build()));
  }
}
