package de.consol.dus.s4.citrus.tests.client;

import static com.consol.citrus.actions.EchoAction.Builder.echo;
import static com.consol.citrus.container.RepeatOnErrorUntilTrue.Builder.repeatOnError;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;
import static com.consol.citrus.variable.MessageHeaderVariableExtractor.Builder.fromHeaders;
import static com.google.common.truth.Truth.assertThat;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.http.client.HttpClientBuilder;
import de.consol.dus.s4.citrus.tests.client.request.DeleteUploadRequest;
import de.consol.dus.s4.citrus.tests.client.request.GetUploadContentRequest;
import de.consol.dus.s4.citrus.tests.client.request.SetTotalPartsRequest;
import de.consol.dus.s4.citrus.tests.client.request.StartUploadRequest;
import de.consol.dus.s4.citrus.tests.client.request.UploadFileInPartsRequest;
import de.consol.dus.s4.citrus.tests.client.request.UploadPartRequest;
import de.consol.dus.s4.citrus.tests.client.request.WaitForUploadDoneRequest;
import de.consol.dus.s4.citrus.tests.client.response.GetUploadContentResponse;
import de.consol.dus.s4.citrus.tests.client.response.UploadResponse;
import de.consol.dus.s4.citrus.tests.config.Config;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;

public class RestApiClient {
  private final HttpClient httpClient;

  @Bean
  public static RestApiClient restApiClient(Config config) {
    return new RestApiClient(config);
  }

  public RestApiClient(Config config) {
    httpClient = new HttpClientBuilder()
        .requestUrl(Objects.requireNonNull(config).getSutUrl())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .charset(StandardCharsets.UTF_8.displayName())
        .build();
  }

  public UploadResponse startUpload(StartUploadRequest request) {
    final TestCaseRunner runner = request.runner();
    final String fileName = request.fileName();
    runner.$(echo("Creating new upload"));
    // @formatter:off
    runner.$(http()
        .client(httpClient)
        .send()
            .post("/uploads")
            .message()
                .headers(addCorrelationIdHeader(new HashMap<>(), request.correlationId()))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("""
                      {
                        "fileName": "%s"
                      }
                      """.formatted(fileName)));
    // @formatter:on

    final UUID internalOperationId = UUID.randomUUID();
    final String correlationIdVariableName = internalOperationId + "-correlationId";
    final String uploadIdVariableName = internalOperationId + "-uploadId";
    // @formatter:off
    runner.$(http()
        .client(httpClient)
        .receive()
            .response(HttpStatus.OK)
            .message()
                .header("X-Correlation-ID")
                .type(MediaType.APPLICATION_JSON_VALUE)
                .validate(jsonPath()
                    .expression("$.id", "@isNumber()@")
                    .expression("$.fileName", fileName)
                    .expression("$.partsReceived", 0)
                    .expression("$.status", "UPLOAD_IN_PROGRESS")
                    .expression("$.totalParts", null))
                .extract(fromHeaders()
                    .expression("X-Correlation-ID", correlationIdVariableName))
                .extract(fromBody().expression("$.id", uploadIdVariableName)));
    // @formatter:on

    return new UploadResponse(
        Long.parseLong(request.context().getVariable(uploadIdVariableName)),
        request.context().getVariable(correlationIdVariableName));
  }

  private Map<String, Object> addCorrelationIdHeader(
      Map<String, Object> map,
      String correlationId) {
    if (Objects.nonNull(correlationId)) {
      map.put("X-Correlation-ID", correlationId);
    }
    return map;
  }

  public void uploadFileInParts(UploadFileInPartsRequest request)
      throws IOException {
    constructPartRequests(request).forEach(this::uploadPart);
  }

  private ArrayList<UploadPartRequest> constructPartRequests(UploadFileInPartsRequest request)
      throws IOException {
    final int byteLength = request.content().length;
    final int parts = request.parts();
    final int bytesPerPart = (byteLength + parts - 1) / parts;
    final int howManyOneLess = bytesPerPart * parts - byteLength;
    final int oneByteLessStartingAtIndex = parts - howManyOneLess;
    final ArrayList<UploadPartRequest> requests = new ArrayList<>();
    int offset = 0;
    for (int part = 0; part < parts; ++part) {
      final int partId = part + 1;
      final int byteSize = bytesPerPart - (part < oneByteLessStartingAtIndex ? 0 : 1);
      byte[] contentForPartId = new byte[byteSize];
      System.arraycopy(request.content(), offset, contentForPartId, 0, byteSize);
      offset += byteSize;
      Path tempFile = Files.createTempFile(UUID.randomUUID().toString(), ".tmp");
      Files.write(tempFile, contentForPartId);
      requests.add(new UploadPartRequest(
          request.uploadId(),
          partId,
          tempFile,
          request.correlationId(),
          request.runner(),
          request.context()));
    }
    return requests;
  }

  public UploadResponse uploadPart(UploadPartRequest request) {
    new ClassPathResource("foobar");
    final TestCaseRunner runner = request.runner();
    // @formatter:off
    LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("partNumber", request.partNumber());
    body.add("content", new FileSystemResource(request.content()));
    runner.$(http().client(httpClient)
        .send()
            .post("/uploads/%s/parts".formatted(request.uploadId()))
            .message()
                .headers(addCorrelationIdHeader(new HashMap<>(), request.correlationId()))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .body(body));
    // @formatter:on

    final UUID internalOperationId = UUID.randomUUID();
    final String correlationIdVariableName = internalOperationId + "-correlationId";
    final String uploadIdVariableName = internalOperationId + "-uploadId";
    // @formatter:off
    runner.$(http().client(httpClient)
        .receive()
            .response(HttpStatus.OK)
            .message()
                .header("X-Correlation-ID")
                .type(MediaType.APPLICATION_JSON_VALUE)
                .validate(jsonPath()
                    .expression("$.id", "@isNumber()@")
                    .expression("$.partsReceived", "@isNumber()@")
                    .expression("$.totalParts", null))
                .extract(fromHeaders()
                    .expression("X-Correlation-ID", correlationIdVariableName))
                .extract(fromBody().expression("$.id", uploadIdVariableName)));
    // @formatter:on

    final TestContext context = request.context();
    final String correlationId = context.getVariable(correlationIdVariableName);
    long uploadId = Long.parseLong(context.getVariable(uploadIdVariableName));
    return new UploadResponse(uploadId, correlationId);
  }

  public UploadResponse setTotalParts(SetTotalPartsRequest request) {
    final TestCaseRunner runner = request.runner();
    // @formatter:off
    runner.$(http().client(httpClient)
        .send()
            .put("/uploads/%s/totalParts".formatted(request.uploadId()))
            .message()
                .headers(addCorrelationIdHeader(new HashMap<>(), request.correlationId()))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("""
                {
                  "totalParts": %d
                }
                """.formatted(request.totalParts())));
    // @formatter:on

    final UUID internalOperationId = UUID.randomUUID();
    final String correlationIdVariableName = internalOperationId + "-correlationId";
    final String uploadIdVariableName = internalOperationId + "-uploadId";
    // @formatter:off
    runner.$(http().client(httpClient)
        .receive()
            .response(HttpStatus.OK)
            .message()
                .header("X-Correlation-ID")
                .validate(jsonPath()
                    .expression("$.id", "@isNumber()@")
                    .expression("$.partsReceived", "@isNumber()@")
                    .expression("$.totalParts", request.totalParts()))
                .extract(fromHeaders()
                    .expression("X-Correlation-ID", correlationIdVariableName))
                .extract(fromBody().expression("$.id", uploadIdVariableName)));
    // @formatter:on

    final TestContext context = request.context();
    final String correlationId = context.getVariable(correlationIdVariableName);
    long uploadId = Long.parseLong(context.getVariable(uploadIdVariableName));
    return new UploadResponse(uploadId, correlationId);
  }

  public GetUploadContentResponse getUploadContent(GetUploadContentRequest request) {
    final TestCaseRunner runner = request.runner();
    // @formatter:off
    runner.$(http().client(httpClient)
        .send()
            .get("/uploads/%d/content".formatted(request.uploadId()))
            .message()
                .headers(addCorrelationIdHeader(new HashMap<>(), request.correlationId())));
    // @formatter:on

    final UUID internalOperationId = UUID.randomUUID();
    final String contentDispositionVariableName =
        internalOperationId + HttpHeaders.CONTENT_DISPOSITION;
    final String correlationIdVariableName = internalOperationId + "-correlationId";
    // @formatter:off
    runner.$(http().client(httpClient)
        .receive()
            .response(HttpStatus.OK)
            .message()
                .header("X-Correlation-ID")
                .contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .extract(fromHeaders()
                    .expression(HttpHeaders.CONTENT_DISPOSITION, contentDispositionVariableName))
                .extract(fromHeaders()
                    .expression("X-Correlation-ID", correlationIdVariableName)));
    // @formatter:on

    final TestContext context = request.context();
    final String contentDispositionHeader = context.getVariable(contentDispositionVariableName);
    assertThat(contentDispositionHeader)
        .contains("attachment; filename=\"%s\"".formatted(request.fileName()));
    return new GetUploadContentResponse(context.getVariable(correlationIdVariableName));
  }

  public void waitForUploadDone(WaitForUploadDoneRequest request) {
    final TestCaseRunner runner = request.runner();
    // @formatter:off
    final long uploadId = request.uploadId();
    final int retries = request.retries();
    final String correlationId = request.correlationId();
    runner.$(repeatOnError()
        .until("i = %d".formatted(retries))
        .index("i")
        .autoSleep(request.timeout())
        .actions(
            echo(
                "Checking upload with id %d status ${i} / %d (CorrelationID = %s)".formatted(
                    uploadId,
                    retries,
                    correlationId)),
            http().client(httpClient)
                .send()
                    .get("/uploads/%d".formatted(uploadId))
                        .message()
                            .headers(addCorrelationIdHeader(
                                new HashMap<>(),
                                correlationId)),
            http().client(httpClient)
                .receive()
                    .response(HttpStatus.OK)
                    .message()
                        .header("X-Correlation-ID")
                        .type(MediaType.APPLICATION_JSON_VALUE)
                        .validate(jsonPath().expression("$.status", "DONE")),
            echo("In try ${i} / %d: Upload with id %d is DONE (CorrelationID = %s)".formatted(
                retries,
                uploadId,
                correlationId))));
    // @formatter:on
  }

  public String deleteUpload(DeleteUploadRequest request) {
    // @formatter:off
    final TestCaseRunner runner = request.runner();
    runner.$(http()
        .client(httpClient)
        .send()
            .delete("/uploads/%s".formatted(request.id()))
            .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(addCorrelationIdHeader(new HashMap<>(), request.correlationId())));
    // @formatter:on

    final UUID internalOperationId = UUID.randomUUID();
    final String correlationIdVariableName = internalOperationId + "-correlationId";
    // @formatter:off
    runner.$(http()
        .client(httpClient)
        .receive()
            .response(HttpStatus.NO_CONTENT)
            .message()
                .header("X-Correlation-ID")
                .extract(fromHeaders()
                    .expression("X-Correlation-ID", correlationIdVariableName)));
    // @formatter:on

    return request.context().getVariable(correlationIdVariableName);
  }
}
