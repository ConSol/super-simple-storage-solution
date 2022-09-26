package de.consol.dus.s4.services.rest.api.boundary.rest.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@SuppressWarnings("unused")
@Value
@Jacksonized
@Builder(access = AccessLevel.PACKAGE)
public class StartUploadRequest {
  @NotBlank
  @Size(min = 1, max = 127)
  String fileName;
}
