package de.consol.dus.s4.services.rest.api.usecases.api.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class PartNumberIsBiggerThanTotalParts extends Exception {
  long id;
  int partNumber;
  int totalParts;

  public PartNumberIsBiggerThanTotalParts(
      long id,
      int totalParts,
      int partNumber) {
    super(
        "totalParts for Upload with id %d set to %d, thus a part with number %d cannot be added"
            .formatted(id, partNumber, totalParts));
    this.id = id;
    this.totalParts = totalParts;
    this.partNumber = partNumber;
  }
}
