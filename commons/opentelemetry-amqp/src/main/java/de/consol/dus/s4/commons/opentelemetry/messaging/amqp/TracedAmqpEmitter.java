package de.consol.dus.s4.commons.opentelemetry.messaging.amqp;

import io.opentelemetry.context.Context;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.TracingMetadata;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;

@NoArgsConstructor
public class TracedAmqpEmitter<T> {
  private Emitter<T> emitter;
  private Logger logger;

  protected TracedAmqpEmitter(Emitter<T> emitter, Logger logger) {
    this.emitter = emitter;
    this.logger = logger;
  }

  @WithSpan
  protected void emit(Message<T> message) {
    // @formatter:off
    Uni.createFrom().item(message)
        .onItem()
            .transform(TracedAmqpEmitter::addTraceMetadata)
            .invoke(msg -> logger.debug("Sending: {}", msg))
            .invoke(emitter::send)
        .subscribe()
            .asCompletionStage();
    // @formatter:on
  }

  private static <T> Message<T> addTraceMetadata(Message<T> msg) {
    return msg.addMetadata(TracingMetadata.withCurrent(Context.current()));
  }
}
