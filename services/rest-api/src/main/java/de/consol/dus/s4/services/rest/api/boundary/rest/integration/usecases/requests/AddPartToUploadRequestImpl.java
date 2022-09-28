package de.consol.dus.s4.services.rest.api.boundary.rest.integration.usecases.requests;

import de.consol.dus.s4.services.rest.api.usecases.api.requests.AddPartToUploadRequest;
import java.util.Arrays;
import java.util.Objects;

public record AddPartToUploadRequestImpl(long id, int partNumber, byte[] content)
    implements AddPartToUploadRequest {
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AddPartToUploadRequestImpl that = (AddPartToUploadRequestImpl) o;
    return id == that.id && partNumber == that.partNumber && Arrays.equals(content, that.content);
  }

  @Override
  public int hashCode() {
    return 31 * Objects.hash(id, partNumber) + Arrays.hashCode(content);
  }

  @Override
  public String toString() {
    return "AddPartToUploadRequestImpl{id=%d, parameterNumber=%d, content=%s}"
        .formatted(id, partNumber, Arrays.toString(content));
  }
}
