package de.consol.dus.s4.commons.http.exceptions.responsemapper;

import de.consol.dus.s4.commons.http.exceptions.response.ErrorResponse;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;

@Provider
public class CatchAllExceptionMapper implements ExceptionMapper<RuntimeException> {
  @SuppressWarnings("CdiInjectionPointsInspection")
  @Inject
  Logger logger;

  @Override
  public final Response toResponse(RuntimeException runtimeException) {
    logger.error("Error occurred", runtimeException);
    return Response.serverError()
        .type(MediaType.APPLICATION_JSON_TYPE)
        .entity(new ErrorResponse("Internal Server Error"))
        .build();
  }
}
