package de.consol.dus.s4.commons.http.exceptions;

import jakarta.ws.rs.core.Response;

public class ConflictException extends HttpStatusException {
  public ConflictException(String message, Throwable cause) {
    super(message, cause, Response.Status.CONFLICT);
  }
}
