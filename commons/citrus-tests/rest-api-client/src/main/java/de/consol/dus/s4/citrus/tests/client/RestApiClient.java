package de.consol.dus.s4.citrus.tests.client;

import static com.consol.citrus.actions.EchoAction.Builder.echo;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;
import static com.consol.citrus.variable.MessageHeaderVariableExtractor.Builder.fromHeaders;

import com.consol.citrus.TestActionRunner;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.http.client.HttpClientBuilder;
import de.consol.dus.s4.citrus.tests.client.requests.DeleteUploadRequest;
import de.consol.dus.s4.citrus.tests.client.results.StartUploadResult;
import de.consol.dus.s4.citrus.tests.config.Config;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

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

  public StartUploadResult startUpload(TestActionRunner runner, TestContext context) {
    runner.$(echo("Creating new upload"));
    final UUID internalOperationId = UUID.randomUUID();
    final String correlationIdVariableName = internalOperationId + "-correlationId";
    final String uploadIdVariableName = internalOperationId + "-uploadId";
    final String fileName = "PenPen-%s.png".formatted(internalOperationId);
    // @formatter:off
    runner.$(http()
        .client(httpClient)
        .send()
            .post("/uploads")
            .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("""
                {
                  "fileName": "%s"
                }
                """.formatted(fileName)));
    runner.$(http()
        .client(httpClient)
        .receive()
            .response(HttpStatus.OK)
            .message()
                .type(MediaType.APPLICATION_JSON_VALUE)
                .header("X-Correlation-ID")
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
    final String correlationId = context.getVariable(correlationIdVariableName);
    long uploadId = Long.parseLong(context.getVariable(uploadIdVariableName));
    return new StartUploadResult(uploadId, correlationId);
  }

  public void deleteUpload(
      DeleteUploadRequest request,
      TestCaseRunner runner,
      TestContext context) {
    // @formatter:off
    runner.$(http()
        .client(httpClient)
        .send()
            .delete("/uploads/%s".formatted(request.getId()))
            .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("X-Correlation-ID", request.getCorrelationId()));
    runner.$(http()
        .client(httpClient)
        .receive()
            .response(HttpStatus.NO_CONTENT));
    // @formatter:on
  }
}
