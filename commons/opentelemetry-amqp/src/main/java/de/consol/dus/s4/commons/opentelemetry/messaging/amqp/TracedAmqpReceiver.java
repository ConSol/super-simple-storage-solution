package de.consol.dus.s4.commons.opentelemetry.messaging.amqp;

import de.consol.dus.s4.commons.correlation.http.exceptions.filter.container.RequestFilter;
import io.opentelemetry.context.Scope;
import io.quarkus.opentelemetry.runtime.QuarkusContextStorage;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.TracingMetadata;
import io.smallrye.reactive.messaging.amqp.IncomingAmqpMetadata;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.MDC;

@NoArgsConstructor
public abstract class TracedAmqpReceiver<T> {
  @Getter(AccessLevel.PROTECTED)
  private Logger logger;

  private ManagedExecutor executor;

  protected TracedAmqpReceiver(Logger logger, ManagedExecutor executor) {
    this.logger = logger;
    this.executor = executor;
  }

  protected abstract void callback(Message<T> message);

  protected CompletionStage<Void> extractContextAndCallback(Message<T> received) {
    final String correlationId = received.getMetadata(IncomingAmqpMetadata.class)
        .map(IncomingAmqpMetadata::getCorrelationId)
        .orElseGet(() -> UUID.randomUUID().toString());
    MDC.put(RequestFilter.CORRELATION_ID_MDC_KEY, correlationId);
    // @formatter:pff
    return Uni.createFrom().item(received)
        .emitOn(executor)
        .onItem()
            .transform(m -> {
              final Optional<Scope> maybeScope = received.getMetadata(TracingMetadata.class)
                  .map(metadata ->
                      QuarkusContextStorage.INSTANCE.attach(metadata.getCurrentContext()));
              callback(m);
              return maybeScope;
            })
        .invoke(received::ack)
        .onFailure()
            .invoke(received::nack)
        .onItem()
            .invoke(maybeScope -> maybeScope.ifPresent(Scope::close))
        .replaceWithVoid()
        .subscribeAsCompletionStage();
    // @formatter:on
  }
}
