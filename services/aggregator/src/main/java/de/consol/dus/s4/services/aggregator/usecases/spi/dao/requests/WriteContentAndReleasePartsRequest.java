package de.consol.dus.s4.services.aggregator.usecases.spi.dao.requests;

import lombok.Value;

@Value
public class WriteContentAndReleasePartsRequest {
  long id;
  byte[] content;
}
