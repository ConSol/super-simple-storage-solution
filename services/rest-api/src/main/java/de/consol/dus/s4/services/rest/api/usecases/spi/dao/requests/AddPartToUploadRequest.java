package de.consol.dus.s4.services.rest.api.usecases.spi.dao.requests;

public interface AddPartToUploadRequest {
  long getId();

  int getPartNumber();

  byte[] getContent();
}
