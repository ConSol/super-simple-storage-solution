package de.consol.dus.s4.services.aggregator.boundary.messaging.amqp;

import de.consol.dus.s4.commons.correlation.http.exceptions.filter.container.RequestFilter;
import de.consol.dus.s4.commons.opentelemetry.messaging.amqp.TracedAmqpReceiver;
import de.consol.dus.s4.services.aggregator.boundary.messaging.amqp.messages.UploadReadyForProcessing;
import de.consol.dus.s4.services.aggregator.boundary.messaging.amqp.requests.AggregateUploadDataRequestImpl;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.smallrye.reactive.messaging.annotations.Blocking;
import java.util.concurrent.CompletionStage;
import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.MDC;

@ApplicationScoped
public class UploadReadyForProcessingReceiver extends TracedAmqpReceiver<UploadReadyForProcessing> {
  public UploadReadyForProcessingReceiver(
      Logger logger,
      @SuppressWarnings("CdiInjectionPointsInspection") ManagedExecutor executor) {
    super(logger, executor);
  }

  @Incoming("amqp-upload-ready-for-processing-incoming")
  @Blocking
  protected CompletionStage<Void> receive(Message<UploadReadyForProcessing> message) {
    return extractContextAndCallback(message);
  }

  @WithSpan
  @Override
  protected void callback(Message<UploadReadyForProcessing> message) {
    Span.current().setAttribute(
        RequestFilter.CORRELATION_ID_MDC_KEY,
        MDC.get(RequestFilter.CORRELATION_ID_MDC_KEY));
    new AggregateUploadDataRequestImpl(message.getPayload().getId()).execute();
  }
}
