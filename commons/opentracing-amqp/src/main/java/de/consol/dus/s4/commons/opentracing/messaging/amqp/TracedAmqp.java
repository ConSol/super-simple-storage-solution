package de.consol.dus.s4.commons.opentracing.messaging.amqp;

import de.consol.dus.s4.commons.opentracing.messaging.Traced;

interface TracedAmqp extends Traced {
  default String getComponentName() {
    return "java-amqp";
  }

  default String getPeerServiceName() {
    return "jms";
  }
}
