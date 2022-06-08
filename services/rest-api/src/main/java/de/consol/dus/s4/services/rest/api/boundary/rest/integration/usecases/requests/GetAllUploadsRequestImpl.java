package de.consol.dus.s4.services.rest.api.boundary.rest.integration.usecases.requests;

import de.consol.dus.s4.services.rest.api.usecases.api.requests.GetAllUploadsRequest;
import lombok.ToString;
import lombok.Value;

@Value
@ToString(exclude = "correlationId")
public class GetAllUploadsRequestImpl implements GetAllUploadsRequest {
  String correlationId;
}
