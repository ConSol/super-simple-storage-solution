package de.consol.dus.s4.services.aggregator.usecases.spi.dao.requests;

import java.util.Arrays;
import java.util.Objects;

public record WriteContentAndReleasePartsRequest(long id, byte[] content) {
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WriteContentAndReleasePartsRequest that = (WriteContentAndReleasePartsRequest) o;
    return id == that.id && Arrays.equals(content, that.content);
  }

  @Override
  public int hashCode() {
    return 31 * Objects.hash(id) + Arrays.hashCode(content);
  }

  @Override
  public String toString() {
    return "WriteContentAndReleasePartsRequest{id=%d, content=%s}".formatted(
        id,
        Arrays.toString(content));
  }
}
