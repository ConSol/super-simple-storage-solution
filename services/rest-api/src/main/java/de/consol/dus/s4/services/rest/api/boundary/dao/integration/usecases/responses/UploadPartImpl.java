package de.consol.dus.s4.services.rest.api.boundary.dao.integration.usecases.responses;

import de.consol.dus.s4.services.rest.api.boundary.dao.entity.UploadPartEntity;
import de.consol.dus.s4.services.rest.api.usecases.spi.dao.responses.UploadPart;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;

@AllArgsConstructor
public class UploadPartImpl implements UploadPart {
  @Delegate(types = UploadPart.class)
  private final UploadPartEntity delegate;
}
