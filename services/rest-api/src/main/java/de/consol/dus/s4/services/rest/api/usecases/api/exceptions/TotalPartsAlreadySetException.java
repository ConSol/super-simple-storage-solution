package de.consol.dus.s4.services.rest.api.usecases.api.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class TotalPartsAlreadySetException extends Exception {
  long id;
  int totalParts;
  String correlationId;

  public TotalPartsAlreadySetException(long id, int totalParts, String correlationId) {
    super("Attribute \"totalParts\" of Upload with id %d already set (to %d)".formatted(
        id,
        totalParts));
    this.id = id;
    this.totalParts = totalParts;
    this.correlationId = correlationId;
  }
}
