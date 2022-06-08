package de.consol.dus.s4.services.rest.api.usecases.api.exceptions;

import de.consol.dus.s4.commons.correlation.Correlated;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class TotalPartsSmallerThanMaxPartNumberException extends Exception implements Correlated {
  long id;
  int totalParts;
  int maxPartNumber;
  String correlationId;

  public TotalPartsSmallerThanMaxPartNumberException(
      long id,
      int totalParts,
      int maxPartNumber,
      String correlationId) {
    super(("Attribute \"totalParts\" of Upload with id %d cannot bet set to %d since the largest"
        + "part number is already %d").formatted(
        id,
        totalParts,
        maxPartNumber));
    this.id = id;
    this.totalParts = totalParts;
    this.maxPartNumber = maxPartNumber;
    this.correlationId = correlationId;
  }
}
