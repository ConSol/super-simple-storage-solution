package de.consol.dus.s4.citrus.tests.client.request;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.context.TestContext;

public record SetTotalPartsRequest(
    long uploadId,
    int totalParts,
    String correlationId,
    TestCaseRunner runner,
    TestContext context) {
}
