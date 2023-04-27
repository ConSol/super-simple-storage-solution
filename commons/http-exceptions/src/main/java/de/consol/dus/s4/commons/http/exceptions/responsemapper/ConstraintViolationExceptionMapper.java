package de.consol.dus.s4.commons.http.exceptions.responsemapper;

import de.consol.dus.s4.commons.http.exceptions.response.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Optional;

@Provider
@SuppressWarnings("unused")
public class ConstraintViolationExceptionMapper implements
    ExceptionMapper<ConstraintViolationException> {

  public static final String BODY_FORMAT = "Parameter \"%s\": %s%n";
  public static final String UNNAMED_PROPERTY = "(unnamed)";

  @Override
  public Response toResponse(ConstraintViolationException exception) {
    StringBuilder message = new StringBuilder();
    for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
      message.append(constructViolationDescription(
          getPropertyNameFromPath(violation).orElse(UNNAMED_PROPERTY),
          violation.getMessage()));
    }
    return Response.status(Status.BAD_REQUEST)
        .type(MediaType.APPLICATION_JSON_TYPE)
        .entity(new ErrorResponse(message.toString()))
        .build();
  }

  private String constructViolationDescription(
      String propertyName,
      String message) {
    return String.format(BODY_FORMAT, propertyName, message);
  }

  private Optional<String> getPropertyNameFromPath(ConstraintViolation<?> violation) {
    String propertyName = null;
    for (Path.Node node : violation.getPropertyPath()) {
      propertyName = node.getName();
    }
    return Optional.ofNullable(propertyName);
  }
}
