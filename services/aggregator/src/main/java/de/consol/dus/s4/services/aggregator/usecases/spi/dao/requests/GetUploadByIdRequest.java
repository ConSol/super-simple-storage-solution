package de.consol.dus.s4.services.aggregator.usecases.spi.dao.requests;

import de.consol.dus.s4.commons.correlation.Correlated;

public interface GetUploadByIdRequest extends Correlated {
  long getId();
}
