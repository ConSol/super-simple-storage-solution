package de.consol.dus.s4.citrus.tests.client.request;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.context.TestContext;

public record WaitForUploadDoneRequest(
    long uploadId,
    String correlationId,
    int retries,
    int timeout,
    TestCaseRunner runner,
    TestContext context) {
}
