package de.consol.dus.s4.citrus.tests.client.requests;

import lombok.Value;

@Value
public class DeleteUploadRequest {
  long id;
  String correlationId;
}
