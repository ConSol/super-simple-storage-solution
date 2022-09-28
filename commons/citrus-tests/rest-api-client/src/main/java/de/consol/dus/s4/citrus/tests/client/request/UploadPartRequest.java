package de.consol.dus.s4.citrus.tests.client.request;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.context.TestContext;
import java.nio.file.Path;

public record UploadPartRequest(
    long uploadId,
    int partNumber,
    Path content,
    String correlationId,
    TestCaseRunner runner,
    TestContext context) {
}
