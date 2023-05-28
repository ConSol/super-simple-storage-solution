package de.consol.dus.s4.services.rest.api.boundary.rest.openapi;

import de.consol.dus.s4.commons.correlation.http.exceptions.filter.container.RequestFilter;
import io.smallrye.openapi.api.models.parameters.ParameterImpl;
import org.eclipse.microprofile.openapi.OASFilter;
import org.eclipse.microprofile.openapi.models.OpenAPI;
import org.eclipse.microprofile.openapi.models.PathItem;
import org.eclipse.microprofile.openapi.models.parameters.Parameter;

@SuppressWarnings("unused")
public class CorrelationIdOasFilter implements OASFilter {

  public static final Parameter CORRELATION_ID_PARAMETER = new ParameterImpl()
      .ref(RequestFilter.CORRELATION_ID_HEADER_KEY);

  @Override
  public void filterOpenAPI(OpenAPI openApi) {
    openApi.getPaths()
        .getPathItems()
        .values()
        .forEach(CorrelationIdOasFilter::addCorrelationIdHeader);
  }

  private static void addCorrelationIdHeader(PathItem pathItem) {
    pathItem.addParameter(CORRELATION_ID_PARAMETER);
  }
}
