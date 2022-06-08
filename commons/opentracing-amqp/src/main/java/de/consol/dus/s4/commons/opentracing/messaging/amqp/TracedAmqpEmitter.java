package de.consol.dus.s4.commons.opentracing.messaging.amqp;

import de.consol.dus.s4.commons.opentracing.messaging.Traced;
import de.consol.dus.s4.commons.opentracing.messaging.TracedEmitter;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.amqp.OutgoingAmqpMetadata;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;

@NoArgsConstructor
public abstract class TracedAmqpEmitter<T> extends TracedEmitter implements TracedAmqp {
  private Emitter<T> emitter;
  private Logger logger;

  protected TracedAmqpEmitter(Tracer tracer, Emitter<T> emitter, Logger logger) {
    super(tracer);
    this.emitter = emitter;
    this.logger = logger;
  }

  protected void emit(Message<T> message) {
    emit(message, OutgoingAmqpMetadata.builder());
  }

  protected void emit(
      Message<T> message,
      OutgoingAmqpMetadata.OutgoingAmqpMetadataBuilder builder) {
    final Span sendSpan = createAndStartSendSpan();
    // @formatter:off
    Uni.createFrom().item(message)
        .onItem()
            .transform(msg -> addUberTraceId(msg, builder, sendSpan))
            .invoke(msg -> logger.debug("Sending: {}", msg))
            .invoke(emitter::send)
        .subscribe()
            .asCompletionStage()
        .thenRun(sendSpan::finish);
    // @formatter:on
  }

  private static <T> Message<T> addUberTraceId(
      Message<T> msg,
      OutgoingAmqpMetadata.OutgoingAmqpMetadataBuilder builder,
      Span sendSpan) {
    return msg.addMetadata(builder
        .withMessageAnnotations(Traced.UBER_TRACE_ID_ANNOTATION_KEY, extractUberTraceId(sendSpan))
        .build());
  }
}
