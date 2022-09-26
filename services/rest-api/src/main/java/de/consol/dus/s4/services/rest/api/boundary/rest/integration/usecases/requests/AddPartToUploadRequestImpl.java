package de.consol.dus.s4.services.rest.api.boundary.rest.integration.usecases.requests;

import de.consol.dus.s4.services.rest.api.usecases.api.requests.AddPartToUploadRequest;
import lombok.ToString;
import lombok.Value;

@Value
@ToString(exclude = {"content"})
public class AddPartToUploadRequestImpl implements AddPartToUploadRequest {
  long id;
  int partNumber;
  byte[] content;
}
