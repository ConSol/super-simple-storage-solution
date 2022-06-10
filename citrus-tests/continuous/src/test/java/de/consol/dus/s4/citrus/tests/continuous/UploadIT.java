package de.consol.dus.s4.citrus.tests.continuous;

import static com.consol.citrus.actions.EchoAction.Builder.echo;
import static com.consol.citrus.actions.SleepAction.Builder.sleep;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import de.consol.dus.s4.citrus.tests.client.RestApiClient;
import de.consol.dus.s4.citrus.tests.client.request.DeleteUploadRequest;
import de.consol.dus.s4.citrus.tests.client.request.GetUploadContentRequest;
import de.consol.dus.s4.citrus.tests.client.request.SetTotalPartsRequest;
import de.consol.dus.s4.citrus.tests.client.request.StartUploadRequest;
import de.consol.dus.s4.citrus.tests.client.request.UploadFileInPartsRequest;
import de.consol.dus.s4.citrus.tests.client.request.WaitForUploadDoneRequest;
import de.consol.dus.s4.citrus.tests.client.response.UploadResponse;
import de.consol.dus.s4.citrus.tests.continuous.configuration.tests.TestConfig;
import java.io.IOException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class UploadIT extends TestNGCitrusSpringSupport {
  @Autowired
  private TestConfig config;

  @Autowired
  private RestApiClient restApiClient;

  @Test
  @Parameters({"runner", "context"})
  @CitrusTest
  public void keepUploading(
      @Optional @CitrusResource TestCaseRunner runner,
      @Optional @CitrusResource TestContext context) throws IOException {
    runner.$(echo(config.getSutUrl()));
    String fileName = "PenPen-%s.png".formatted(UUID.randomUUID());
    while (true) {
      final UploadResponse result = restApiClient.startUpload(new StartUploadRequest(
          fileName,
          runner,
          context,
          null));
      long uploadId = result.getUploadId();
      final String correlationId = result.getCorrelationId();
      runner.$(echo("Started upload with id %s (CorrelationId = %s)".formatted(
          uploadId,
          correlationId)));

      final String fileNameToLoad = config.getFileToUpload();
      int parts = config.getNPartsPerUpload();
      final byte[] fileContentToUpload =
          new ClassPathResource(config.getFileToUpload()).getInputStream().readAllBytes();
      restApiClient.uploadFileInParts(new UploadFileInPartsRequest(
          uploadId,
          parts,
          fileContentToUpload,
          correlationId,
          runner,
          context));
      runner.$(echo("Uploaded file %s in %d parts as %s to upload with id %d (CorrelationID = %s)"
          .formatted(fileName, parts, fileNameToLoad, uploadId, correlationId)));

      restApiClient.setTotalParts(new SetTotalPartsRequest(
          uploadId,
          parts,
          correlationId,
          runner,
          context));
      runner.$(echo("Set totalParts of upload with id %d (CorrelationID = %s)".formatted(
          uploadId, correlationId)));

      restApiClient.waitForUploadDone(
          new WaitForUploadDoneRequest(uploadId, correlationId, 10, 100, runner, context));

      restApiClient.getUploadContent(new GetUploadContentRequest(
          fileName,
          uploadId,
          correlationId,
          runner,
          context));

      restApiClient.deleteUpload(new DeleteUploadRequest(uploadId, correlationId, runner, context));
      runner.$(echo("Deleted upload with id %s (CorrelationId = %s)".formatted(
          uploadId,
          correlationId)));

      runner.$(sleep().milliseconds(config.getSleepBetweenUploadsInMs()));
    }
  }
}
