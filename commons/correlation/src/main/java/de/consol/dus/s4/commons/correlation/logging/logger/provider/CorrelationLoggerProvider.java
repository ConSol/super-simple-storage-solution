package de.consol.dus.s4.commons.correlation.logging.logger.provider;

import de.consol.dus.s4.commons.correlation.logging.logger.CorrelationLogger;
import io.quarkus.arc.Priority;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import org.slf4j.Logger;

@Dependent
public class CorrelationLoggerProvider {
  @Produces
  @Alternative
  @Priority(200)
  public Logger logger(InjectionPoint injectionPoint) {
    return new CorrelationLogger(injectionPoint.getMember().getDeclaringClass());
  }
}
