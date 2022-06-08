package de.consol.dus.s4.commons.opentracing.messaging;

public interface Traced {
  String UBER_TRACE_ID_ANNOTATION_KEY = "uber-trace-id";

  String getComponentName();

  String getPeerServiceName();
}
