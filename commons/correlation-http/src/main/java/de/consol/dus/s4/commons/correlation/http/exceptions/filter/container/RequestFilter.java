package de.consol.dus.s4.commons.correlation.http.exceptions.filter.container;

import io.quarkus.vertx.web.RouteFilter;
import io.vertx.ext.web.RoutingContext;
import java.util.Optional;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import org.slf4j.MDC;

@ApplicationScoped
public class RequestFilter {
  public static final String CORRELATION_ID_HEADER_KEY = "X-Correlation-ID";
  public static final String CORRELATION_ID_MDC_KEY = "correlationId";

  @SuppressWarnings("unused")
  @RouteFilter(RouteFilter.DEFAULT_PRIORITY + 1)
  void addCorrelationIdFilter(RoutingContext context) {
    final String correlationId = getOrCreateCorrelationIdFromRequest(context);
    context.request().headers().add(CORRELATION_ID_HEADER_KEY, correlationId);
    context.response().headers().add(CORRELATION_ID_HEADER_KEY, correlationId);
    MDC.put(CORRELATION_ID_MDC_KEY, correlationId);

    context.next();
  }

  private static String getOrCreateCorrelationIdFromRequest(RoutingContext context) {
    final String existingCorrelationId = context.request().getHeader(CORRELATION_ID_HEADER_KEY);
    final String correlationId;
    if (!Optional.ofNullable(existingCorrelationId).map(String::isBlank).orElse(true)) {
      correlationId = existingCorrelationId;
    } else {
      correlationId = UUID.randomUUID().toString();
    }
    return correlationId;
  }
}
