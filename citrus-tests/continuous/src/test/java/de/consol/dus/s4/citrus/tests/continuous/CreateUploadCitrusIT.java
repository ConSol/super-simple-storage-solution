package de.consol.dus.s4.citrus.tests.continuous;

import static com.consol.citrus.actions.EchoAction.Builder.echo;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import de.consol.dus.s4.citrus.tests.client.RestApiClient;
import de.consol.dus.s4.citrus.tests.client.requests.DeleteUploadRequest;
import de.consol.dus.s4.citrus.tests.client.results.StartUploadResult;
import de.consol.dus.s4.citrus.tests.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class CreateUploadCitrusIT extends TestNGCitrusSpringSupport {
  @Autowired
  Config config;

  @Autowired
  RestApiClient restApiClient;

  @Test
  @Parameters({"runner", "context"})
  @CitrusTest
  public void test(
      @Optional @CitrusResource TestCaseRunner runner,
      @Optional @CitrusResource TestContext context) {
    runner.$(echo(config.getSutUrl()));
    StartUploadResult result = restApiClient.startUpload(runner, context);
    runner.$(echo("Started upload with id %s (CorrelationId = %s)".formatted(
        result.getUploadId(),
        result.getCorrelationId())));

    restApiClient.deleteUpload(
        new DeleteUploadRequest(result.getUploadId(), result.getCorrelationId()),
        runner,
        context);
    runner.$(echo("Deleted upload with id %s (CorrelationId = %s)".formatted(
        result.getUploadId(),
        result.getCorrelationId())));
  }
}
