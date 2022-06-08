package de.consol.dus.s4.commons.opentracing.messaging;

import io.jaegertracing.internal.JaegerSpanContext;
import io.opentracing.References;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.tag.Tags;
import java.math.BigInteger;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter(AccessLevel.PROTECTED)
@AllArgsConstructor
@NoArgsConstructor
public abstract class TracedReceiver implements Traced {
  private Tracer tracer;

  protected abstract String getSourceName();

  protected static JaegerSpanContext createJaegerSpanContextFromUberTraceId(String uberTraceId) {
    final String[] parts = uberTraceId.split(":");
    return new JaegerSpanContext(
        high(parts[0]),
        new BigInteger(parts[0], 16).longValue(),
        new BigInteger(parts[1], 16).longValue(),
        new BigInteger(parts[2], 16).longValue(),
        new BigInteger(parts[3], 16).byteValue());
  }

  private static long high(String hexString) {
    final int hexStringLength = hexString.length();
    return hexStringLength > 16
        ? Long.parseLong(hexString.substring(0, hexStringLength - 16), 16)
        : 0L;
  }

  protected Span buildAndStartReceiveSpan(JaegerSpanContext context, String destination) {
    return tracer
        .buildSpan("From_" + getSourceName())
        .withTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_CONSUMER)
        .withTag(Tags.COMPONENT.getKey(), getComponentName())
        .withTag(Tags.MESSAGE_BUS_DESTINATION.getKey(), destination)
        .withTag(Tags.PEER_SERVICE.getKey(), getPeerServiceName())
        .addReference(References.FOLLOWS_FROM, context)
        .start();
  }
}
