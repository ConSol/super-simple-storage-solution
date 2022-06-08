package de.consol.dus.s4.services.rest.api.boundary.rest.integration.usecases.requests;

import de.consol.dus.s4.services.rest.api.usecases.api.requests.SetTotalPartsForUploadRequest;
import lombok.ToString;
import lombok.Value;

@Value
@ToString(exclude = "correlationId")
public class SetTotalPartsForUploadRequestImpl implements SetTotalPartsForUploadRequest {
  long id;
  int totalParts;
  String correlationId;
}
