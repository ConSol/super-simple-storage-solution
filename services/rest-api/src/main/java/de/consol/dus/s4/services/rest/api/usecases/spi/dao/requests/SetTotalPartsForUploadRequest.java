package de.consol.dus.s4.services.rest.api.usecases.spi.dao.requests;

public interface SetTotalPartsForUploadRequest {
  long id();

  int totalParts();
}
