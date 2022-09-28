package de.consol.dus.s4.services.rest.api.usecases.spi.dao.requests;

public interface AddPartToUploadRequest {
  long id();

  int partNumber();

  byte[] content();
}
