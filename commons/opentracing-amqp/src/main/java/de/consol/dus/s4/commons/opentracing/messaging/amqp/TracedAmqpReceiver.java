package de.consol.dus.s4.commons.opentracing.messaging.amqp;

import de.consol.dus.s4.commons.opentracing.messaging.Traced;
import de.consol.dus.s4.commons.opentracing.messaging.TracedReceiver;
import io.jaegertracing.internal.JaegerSpanContext;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.amqp.IncomingAmqpMetadata;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.qpid.proton.amqp.Symbol;
import org.apache.qpid.proton.amqp.messaging.MessageAnnotations;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;

@NoArgsConstructor
public abstract class TracedAmqpReceiver<T> extends TracedReceiver implements TracedAmqp {
  @Getter(AccessLevel.PROTECTED)
  private Logger logger;

  protected TracedAmqpReceiver(Tracer tracer, Logger logger) {
    super(tracer);
    this.logger = logger;
  }

  protected abstract void callback(Message<T> message);

  protected CompletionStage<Void> createSpanAndCallCallback(Message<T> received) {
    final Span receiveSpan = constructAndStartSendSpan(received);
    try (@SuppressWarnings("unused") final Scope receiveScope =
             getTracer().activateSpan(receiveSpan)) {
      // @formatter:off
      return Uni.createFrom().item(received)
          .onItem()
          .invoke(this::callback)
          .map(v -> received.ack())
          .onFailure()
              .invoke(received::nack)
          .invoke(() -> receiveSpan.finish())
          .replaceWithVoid()
          .subscribeAsCompletionStage();
      // @formatter:on
    }
  }

  private Span constructAndStartSendSpan(Message<T> received) {
    final IncomingAmqpMetadata metadata =
        received.getMetadata(IncomingAmqpMetadata.class).orElseThrow();
    final Optional<String> maybeUberTraceId = Optional.of(metadata)
        .map(IncomingAmqpMetadata::getMessageAnnotations)
        .map(MessageAnnotations::getValue)
        .map(map -> map.get(Symbol.getSymbol(Traced.UBER_TRACE_ID_ANNOTATION_KEY)))
        .filter(String.class::isInstance)
        .map(String.class::cast);
    if (maybeUberTraceId.isPresent()) {
      final JaegerSpanContext context =
          TracedReceiver.createJaegerSpanContextFromUberTraceId(maybeUberTraceId.get());
      final String destination = Optional.of(metadata)
          .map(IncomingAmqpMetadata::getAddress)
          .orElse("<UNKNOWN>");
      return buildAndStartReceiveSpan(context, destination);
    }
    logger.warn("Unable to extract uber-trace-id, starting new, uncorrelated span");
    return getTracer().activeSpan();
  }
}
