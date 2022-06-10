package de.consol.dus.s4.citrus.tests.client.response;

import lombok.Value;

@Value
public class UploadResponse {
  long uploadId;
  String correlationId;
}
