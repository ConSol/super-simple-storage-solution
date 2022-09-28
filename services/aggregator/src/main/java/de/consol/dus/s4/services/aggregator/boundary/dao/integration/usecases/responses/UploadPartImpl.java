package de.consol.dus.s4.services.aggregator.boundary.dao.integration.usecases.responses;

import de.consol.dus.s4.services.aggregator.boundary.dao.entity.UploadPartEntity;
import de.consol.dus.s4.services.aggregator.usecases.spi.dao.responses.UploadPart;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;

@AllArgsConstructor
public class UploadPartImpl implements UploadPart {
  @SuppressWarnings("unused")
  @Delegate(types = UploadPart.class)
  private final UploadPartEntity delegate;
}
