package de.consol.dus.s4.citrus.tests.client.results;

import lombok.Value;

@Value
public class StartUploadResult {
  long uploadId;
  String correlationId;
}
