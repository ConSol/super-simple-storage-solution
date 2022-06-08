package de.consol.dus.s4.services.rest.api.usecases.api.exceptions;

import de.consol.dus.s4.commons.correlation.Correlated;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class PartNumberAlreadyExistsException extends Exception implements Correlated {
  long id;
  int partNumber;
  String correlationId;

  public PartNumberAlreadyExistsException(long id, int partNumber, String correlationId) {
    super("Upload with id %d already has a part with number %d".formatted(id, partNumber));
    this.id = id;
    this.partNumber = partNumber;
    this.correlationId = correlationId;
  }
}
