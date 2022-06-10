package de.consol.dus.s4.citrus.tests.client.request;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.context.TestContext;
import lombok.Value;

@Value
public class DeleteUploadRequest {
  long id;
  String correlationId;
  TestCaseRunner runner;
  TestContext context;
}
