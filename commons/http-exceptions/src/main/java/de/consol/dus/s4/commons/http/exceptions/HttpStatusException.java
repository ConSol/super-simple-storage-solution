package de.consol.dus.s4.commons.http.exceptions;

import de.consol.dus.s4.commons.http.exceptions.response.ErrorResponse;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;

@RegisterForReflection
@Value
@NonFinal
@EqualsAndHashCode(callSuper = true)
public class HttpStatusException extends WebApplicationException {

  public HttpStatusException(String message, Response.Status status) {
    this(message, null, status);
  }

  public HttpStatusException(String message, Throwable cause, Response.Status status) {
    super(
        message,
        cause,
        Response
            .status(status)
            .type(MediaType.APPLICATION_JSON_TYPE)
            .entity(new ErrorResponse(message))
            .build());
  }
}
