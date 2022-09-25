package de.consol.dus.s4.services.aggregator.usecases.api.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class SingletonNotInitializedError extends Error {

  public SingletonNotInitializedError(Throwable cause) {
    super(cause);
  }
}
