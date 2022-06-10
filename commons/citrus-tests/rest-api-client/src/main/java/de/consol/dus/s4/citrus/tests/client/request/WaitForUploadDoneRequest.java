package de.consol.dus.s4.citrus.tests.client.request;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.context.TestContext;
import lombok.Value;

@Value
public class WaitForUploadDoneRequest {
  long uploadId;
  String correlationId;
  int retries;
  int timeout;
  TestCaseRunner runner;
  TestContext context;
}
