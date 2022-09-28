package de.consol.dus.s4.citrus.tests.client.request;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.context.TestContext;

public record StartUploadRequest(
    String fileName,
    String correlationId,
    TestCaseRunner runner,
    TestContext context) {
}
