package de.consol.dus.s4.services.rest.api.boundary.rest.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.consol.dus.s4.services.rest.api.usecases.api.responses.Upload;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;
import lombok.experimental.Delegate;

@Value
@JsonIgnoreProperties({"delegate", "content", "parts"})
public class UploadResponse implements Upload {
  @Getter(AccessLevel.NONE)
  @Delegate
  Upload delegate;

  public int getPartsReceived() {
    return delegate.getParts().size();
  }

}
