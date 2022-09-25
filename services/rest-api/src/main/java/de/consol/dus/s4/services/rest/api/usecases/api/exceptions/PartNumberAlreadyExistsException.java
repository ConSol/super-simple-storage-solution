package de.consol.dus.s4.services.rest.api.usecases.api.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class PartNumberAlreadyExistsException extends Exception {
  long id;
  int partNumber;

  public PartNumberAlreadyExistsException(long id, int partNumber) {
    super("Upload with id %d already has a part with number %d".formatted(id, partNumber));
    this.id = id;
    this.partNumber = partNumber;
  }
}
