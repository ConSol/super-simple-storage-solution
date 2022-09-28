package de.consol.dus.s4.citrus.tests.client.request;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.context.TestContext;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public record UploadFileInPartsRequest(
    long uploadId,
    int parts,
    byte[] content,
    String correlationId, TestCaseRunner runner,
    TestContext context) {
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UploadFileInPartsRequest that = (UploadFileInPartsRequest) o;
    return uploadId == that.uploadId && parts == that.parts && Arrays.equals(content,
        that.content) && Objects.equals(correlationId, that.correlationId) && runner.equals(
        that.runner) && context.equals(that.context);
  }

  @Override
  public int hashCode() {
    return
        31 * Objects.hash(uploadId, parts, correlationId, runner, context)
            + Arrays.hashCode(content);
  }

  @Override
  public String toString() {
    return
        ("UploadFileInPartsRequest{uploadId=%d, parts=%d, content=%s, correlationId=%s, runner=%s, "
            + "context=%s}").formatted(
                uploadId,
                parts,
                Optional.ofNullable(content)
                    .map(c -> c.length)
                    .map(Object::toString)
                    .orElse("null"),
                correlationId,
                runner,
                context);
  }
}
