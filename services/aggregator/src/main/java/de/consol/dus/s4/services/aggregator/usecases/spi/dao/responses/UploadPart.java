package de.consol.dus.s4.services.aggregator.usecases.spi.dao.responses;

@SuppressWarnings("unused")
public interface UploadPart {
  int getPartNumber();

  byte[] getContent();
}