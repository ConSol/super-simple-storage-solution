package de.consol.dus.s4.services.rest.api.boundary.rest.integration.usecases.requests;

import de.consol.dus.s4.services.rest.api.usecases.api.requests.CreateNewUploadRequest;
import lombok.ToString;
import lombok.Value;

@Value
@ToString
public class CreateNewUploadRequestImpl implements CreateNewUploadRequest {
  String fileName;
}
