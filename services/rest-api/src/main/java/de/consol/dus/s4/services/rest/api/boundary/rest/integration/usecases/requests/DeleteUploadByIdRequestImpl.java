package de.consol.dus.s4.services.rest.api.boundary.rest.integration.usecases.requests;

import de.consol.dus.s4.services.rest.api.usecases.api.requests.DeleteUploadByIdRequest;
import lombok.ToString;
import lombok.Value;

@Value
@ToString
public class DeleteUploadByIdRequestImpl implements DeleteUploadByIdRequest {
  long id;
}
