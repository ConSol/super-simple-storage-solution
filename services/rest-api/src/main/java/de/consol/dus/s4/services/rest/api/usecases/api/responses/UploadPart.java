package de.consol.dus.s4.services.rest.api.usecases.api.responses;

public interface UploadPart {
  int getPartNumber();

  byte[] getContent();
}
