package de.consol.dus.s4.services.aggregator.usecases.api.exceptions;

import de.consol.dus.s4.commons.correlation.Correlated;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class SingletonNotInitializedError extends Error implements Correlated {
  String correlationId;

  public SingletonNotInitializedError(Throwable cause, String correlationId) {
    super(cause);
    this.correlationId = correlationId;
  }
}
