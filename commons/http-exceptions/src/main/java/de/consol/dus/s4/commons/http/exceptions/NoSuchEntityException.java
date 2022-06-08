package de.consol.dus.s4.commons.http.exceptions;

import javax.ws.rs.core.Response;

public class NoSuchEntityException extends HttpStatusException {
  public NoSuchEntityException(Class<?> clazz, long id) {
    super(
        "No %s with id %d exists".formatted(clazz.getSimpleName(), id),
        Response.Status.NOT_FOUND
    );
  }

  public NoSuchEntityException(String message) {
    super(message, Response.Status.NOT_FOUND);
  }
}
