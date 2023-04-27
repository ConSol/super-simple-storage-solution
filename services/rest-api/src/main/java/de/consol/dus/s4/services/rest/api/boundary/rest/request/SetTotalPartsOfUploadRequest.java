package de.consol.dus.s4.services.rest.api.boundary.rest.request;

import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@SuppressWarnings("unused")
@Value
@Jacksonized
@Builder(access = AccessLevel.PACKAGE)
public class SetTotalPartsOfUploadRequest {
  @Min(value = 1)
  int totalParts;
}
