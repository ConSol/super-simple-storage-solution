package de.consol.dus.s4.commons.injection;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class LoggerInjection {

  /**
   * Producer enabling logger injection.
   *
   * @param injectionPoint
   *     injectionPoint.
   *
   * @return the logger instance to inject.
   */
  @Produces
  public Logger logger(InjectionPoint injectionPoint) {
    return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
  }
}
