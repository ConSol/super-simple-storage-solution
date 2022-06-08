package de.consol.dus.s4.commons.opentracing.messaging;

import io.jaegertracing.internal.JaegerSpanContext;
import io.opentracing.References;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.tag.Tags;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public abstract class TracedEmitter implements Traced {
  private Tracer tracer;

  protected abstract String getDestinationName();

  protected static String extractUberTraceId(Span span) {
    final JaegerSpanContext context = ((JaegerSpanContext) span.context());
    return String.format(
        "%s:%s:%s:%s",
        context.getTraceId(),
        Long.toHexString(context.getSpanId()),
        Long.toHexString(context.getParentId()),
        Integer.toHexString(context.getFlags()));
  }

  protected Span createAndStartSendSpan() {
    return tracer.buildSpan("To_" + getDestinationName())
        .addReference(References.CHILD_OF, tracer.activeSpan().context())
        .withTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_PRODUCER)
        .withTag(Tags.COMPONENT.getKey(), getComponentName())
        .withTag(Tags.MESSAGE_BUS_DESTINATION.getKey(), getDestinationName())
        .withTag(Tags.PEER_SERVICE.getKey(), getPeerServiceName())
        .start();
  }
}
