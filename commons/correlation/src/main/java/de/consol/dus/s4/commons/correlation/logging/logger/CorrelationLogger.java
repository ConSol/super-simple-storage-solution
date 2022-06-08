package de.consol.dus.s4.commons.correlation.logging.logger;

import de.consol.dus.s4.commons.correlation.Correlated;
import java.util.Arrays;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Delegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.Marker;

@Getter(AccessLevel.PRIVATE)
public class CorrelationLogger implements Logger {
  private static final String CORRELATION_ID_KEY = "CorrelationID";

  @Delegate(excludes = Exclusion.class)
  private final Logger actual;

  public CorrelationLogger(Class<?> clazz) {
    this.actual = LoggerFactory.getLogger(clazz);
  }

  @Override
  public void trace(String format, Object arg) {
    setupMdc(arg);
    getActual().trace(format, arg);
    removeFromMdc();
  }

  @Override
  public void trace(String format, Object arg1, Object arg2) {
    setupMdc(arg1, arg2);
    getActual().trace(format, arg1, arg2);
    removeFromMdc();
  }
  
  @Override
  public void trace(String format, Object... arguments) {
    setupMdc(arguments);
    getActual().trace(format, arguments);
    removeFromMdc();
  }

  @Override
  public void trace(Marker marker, String format, Object arg) {
    setupMdc(arg);
    getActual().trace(marker, format, arg);
    removeFromMdc();
  }
  
  @Override
  public void trace(Marker marker, String format, Object arg1, Object arg2) {
    setupMdc(arg1, arg2);
    getActual().trace(marker, format, arg1, arg2);
    removeFromMdc();
  }
  
  @Override
  public void trace(Marker marker, String format, Object... argArray) {
    setupMdc(argArray);
    getActual().trace(marker, format, argArray);
    removeFromMdc();
  }

  @Override
  public void debug(String format, Object arg) {
    setupMdc(arg);
    getActual().debug(format, arg);
    removeFromMdc();
  }

  @Override
  public void debug(String format, Object arg1, Object arg2) {
    setupMdc(arg1, arg2);
    getActual().debug(format, arg1, arg2);
    removeFromMdc();
  }

  @Override
  public void debug(String format, Object... arguments) {
    setupMdc(arguments);
    getActual().debug(format, arguments);
    removeFromMdc();
  }

  @Override
  public void debug(Marker marker, String format, Object arg) {
    setupMdc(arg);
    getActual().debug(marker, format, arg);
    removeFromMdc();
  }

  @Override
  public void debug(Marker marker, String format, Object arg1, Object arg2) {
    setupMdc(arg1, arg2);
    getActual().debug(marker, format, arg1, arg2);
    removeFromMdc();
  }

  @Override
  public void debug(Marker marker, String format, Object... argArray) {
    setupMdc(argArray);
    getActual().debug(marker, format, argArray);
    removeFromMdc();
  }
  
  @Override
  public void info(String format, Object arg) {
    setupMdc(arg);
    getActual().info(format, arg);
    removeFromMdc();
  }

  @Override
  public void info(String format, Object arg1, Object arg2) {
    setupMdc(arg1, arg2);
    getActual().info(format, arg1, arg2);
    removeFromMdc();
  }

  @Override
  public void info(String format, Object... arguments) {
    setupMdc(arguments);
    getActual().info(format, arguments);
    removeFromMdc();
  }

  @Override
  public void info(Marker marker, String format, Object arg) {
    setupMdc(arg);
    getActual().info(marker, format, arg);
    removeFromMdc();
  }

  @Override
  public void info(Marker marker, String format, Object arg1, Object arg2) {
    setupMdc(arg1, arg2);
    getActual().info(marker, format, arg1, arg2);
    removeFromMdc();
  }

  @Override
  public void info(Marker marker, String format, Object... argArray) {
    setupMdc(argArray);
    getActual().info(marker, format, argArray);
    removeFromMdc();
  }
  
  @Override
  public void warn(String format, Object arg) {
    setupMdc(arg);
    getActual().warn(format, arg);
    removeFromMdc();
  }

  @Override
  public void warn(String format, Object arg1, Object arg2) {
    setupMdc(arg1, arg2);
    getActual().warn(format, arg1, arg2);
    removeFromMdc();
  }

  @Override
  public void warn(String format, Object... arguments) {
    setupMdc(arguments);
    getActual().warn(format, arguments);
    removeFromMdc();
  }

  @Override
  public void warn(Marker marker, String format, Object arg) {
    setupMdc(arg);
    getActual().warn(marker, format, arg);
    removeFromMdc();
  }

  @Override
  public void warn(Marker marker, String format, Object arg1, Object arg2) {
    setupMdc(arg1, arg2);
    getActual().warn(marker, format, arg1, arg2);
    removeFromMdc();
  }

  @Override
  public void warn(Marker marker, String format, Object... argArray) {
    setupMdc(argArray);
    getActual().warn(marker, format, argArray);
    removeFromMdc();
  }

  @Override
  public void error(String format, Object arg) {
    setupMdc(arg);
    getActual().error(format, arg);
    removeFromMdc();
  }

  @Override
  public void error(String format, Object arg1, Object arg2) {
    setupMdc(arg1, arg2);
    getActual().error(format, arg1, arg2);
    removeFromMdc();
  }

  @Override
  public void error(String format, Object... arguments) {
    setupMdc(arguments);
    getActual().error(format, arguments);
    removeFromMdc();
  }

  @Override
  public void error(Marker marker, String format, Object arg) {
    setupMdc(arg);
    getActual().error(marker, format, arg);
    removeFromMdc();
  }

  @Override
  public void error(Marker marker, String format, Object arg1, Object arg2) {
    setupMdc(arg1, arg2);
    getActual().error(marker, format, arg1, arg2);
    removeFromMdc();
  }

  @Override
  public void error(Marker marker, String format, Object... argArray) {
    setupMdc(argArray);
    getActual().error(marker, format, argArray);
    removeFromMdc();
  }

  private void setupMdc(Object... objects) {
    Arrays.stream(Optional.ofNullable(objects).orElse(new Object[0]))
        .filter(Correlated.class::isInstance)
        .map(Correlated.class::cast)
        .findFirst()
        .ifPresent(this::putCorrelationIdIntoMdc);
  }

  private void putCorrelationIdIntoMdc(Correlated correlated) {
    MDC.put(CORRELATION_ID_KEY, correlated.getCorrelationId());
  }

  private void removeFromMdc() {
    MDC.remove(CORRELATION_ID_KEY);
  }

  @SuppressWarnings("unused")
  private interface Exclusion {
    void trace(String format, Object arg);

    void trace(String format, Object arg1, Object arg2);

    void trace(String format, Object... arguments);

    void trace(Marker marker, String format, Object arg);

    void trace(Marker marker, String format, Object arg1, Object arg2);

    void trace(Marker marker, String format, Object... argArray);

    void debug(String format, Object arg);

    void debug(String format, Object arg1, Object arg2);

    void debug(String format, Object... arguments);

    void debug(Marker marker, String format, Object arg);

    void debug(Marker marker, String format, Object arg1, Object arg2);

    void debug(Marker marker, String format, Object... argArray);

    void info(String format, Object arg);

    void info(String format, Object arg1, Object arg2);

    void info(String format, Object... arguments);

    void info(Marker marker, String format, Object arg);

    void info(Marker marker, String format, Object arg1, Object arg2);

    void info(Marker marker, String format, Object... argArray);

    void warn(String format, Object arg);

    void warn(String format, Object arg1, Object arg2);

    void warn(String format, Object... arguments);

    void warn(Marker marker, String format, Object arg);

    void warn(Marker marker, String format, Object arg1, Object arg2);

    void warn(Marker marker, String format, Object... argArray);

    void error(String format, Object arg);

    void error(String format, Object arg1, Object arg2);

    void error(String format, Object... arguments);

    void error(Marker marker, String format, Object arg);

    void error(Marker marker, String format, Object arg1, Object arg2);

    void error(Marker marker, String format, Object... argArray);
  }
}
