package de.consol.dus.s4.services.aggregator.usecases.spi.dao.requests;

import de.consol.dus.s4.commons.correlation.Correlated;
import lombok.Value;

@Value
public class WriteContentAndReleasePartsRequest implements Correlated {
  long id;
  byte[] content;
  String correlationId;
}
