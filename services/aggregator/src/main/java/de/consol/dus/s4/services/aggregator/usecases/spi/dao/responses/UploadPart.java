package de.consol.dus.s4.services.aggregator.usecases.spi.dao.responses;

public interface UploadPart {
  int getPartNumber();

  byte[] getContent();
}