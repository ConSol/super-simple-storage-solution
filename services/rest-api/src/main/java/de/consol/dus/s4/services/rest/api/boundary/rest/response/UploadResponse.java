package de.consol.dus.s4.services.rest.api.boundary.rest.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.consol.dus.s4.services.rest.api.usecases.api.responses.Upload;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;
import lombok.experimental.Delegate;

@SuppressWarnings("unused")
@Value
@JsonIgnoreProperties({"delegate", "content", "parts"})
public class UploadResponse implements Upload {
  @Getter(AccessLevel.PRIVATE)
  @Delegate
  Upload delegate;

  public int getPartsReceived() {
    return getDelegate().getParts().size();
  }
}
