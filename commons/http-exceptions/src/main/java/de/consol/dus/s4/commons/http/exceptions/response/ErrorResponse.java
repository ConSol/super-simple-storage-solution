package de.consol.dus.s4.commons.http.exceptions.response;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Value;

@RegisterForReflection
@Value
public class ErrorResponse {
  String message;
}
