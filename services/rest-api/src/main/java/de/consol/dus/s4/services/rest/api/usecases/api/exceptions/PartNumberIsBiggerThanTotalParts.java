package de.consol.dus.s4.services.rest.api.usecases.api.exceptions;

import de.consol.dus.s4.commons.correlation.Correlated;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class PartNumberIsBiggerThanTotalParts extends Exception implements Correlated {
  String correlationId;
  long id;
  int partNumber;
  int totalParts;

  public PartNumberIsBiggerThanTotalParts(
      long id,
      int totalParts,
      int partNumber,
      String correlationId) {
    super(
        "totalParts for Upload with id %d set to %d, thus a part with number %d cannot be added"
            .formatted(id, partNumber, totalParts));
    this.id = id;
    this.totalParts = totalParts;
    this.partNumber = partNumber;
    this.correlationId = correlationId;
  }
}
