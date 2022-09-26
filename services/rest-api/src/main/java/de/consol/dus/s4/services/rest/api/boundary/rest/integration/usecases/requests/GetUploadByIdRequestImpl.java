package de.consol.dus.s4.services.rest.api.boundary.rest.integration.usecases.requests;

import de.consol.dus.s4.services.rest.api.usecases.api.requests.GetUploadByIdRequest;
import lombok.ToString;
import lombok.Value;

@Value
@ToString
public class GetUploadByIdRequestImpl implements GetUploadByIdRequest {
  long id;
}
