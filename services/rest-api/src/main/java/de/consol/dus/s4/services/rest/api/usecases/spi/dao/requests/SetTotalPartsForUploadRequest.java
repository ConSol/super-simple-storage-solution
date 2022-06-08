package de.consol.dus.s4.services.rest.api.usecases.spi.dao.requests;

import de.consol.dus.s4.commons.correlation.Correlated;

public interface SetTotalPartsForUploadRequest extends Correlated {
  long getId();

  int getTotalParts();
}
