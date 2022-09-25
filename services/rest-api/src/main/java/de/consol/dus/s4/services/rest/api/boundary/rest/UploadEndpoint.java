package de.consol.dus.s4.services.rest.api.boundary.rest;

import de.consol.dus.s4.commons.correlation.http.exceptions.filter.container.RequestFilter;
import de.consol.dus.s4.commons.http.exceptions.ConflictException;
import de.consol.dus.s4.commons.http.exceptions.NoSuchEntityException;
import de.consol.dus.s4.services.rest.api.boundary.rest.integration.usecases.requests.AddPartToUploadRequestImpl;
import de.consol.dus.s4.services.rest.api.boundary.rest.integration.usecases.requests.CreateNewUploadRequestImpl;
import de.consol.dus.s4.services.rest.api.boundary.rest.integration.usecases.requests.DeleteUploadByIdRequestImpl;
import de.consol.dus.s4.services.rest.api.boundary.rest.integration.usecases.requests.GetAllUploadsRequestImpl;
import de.consol.dus.s4.services.rest.api.boundary.rest.integration.usecases.requests.GetUploadByIdRequestImpl;
import de.consol.dus.s4.services.rest.api.boundary.rest.integration.usecases.requests.SetTotalPartsForUploadRequestImpl;
import de.consol.dus.s4.services.rest.api.boundary.rest.request.AddPartToUploadRequest;
import de.consol.dus.s4.services.rest.api.boundary.rest.request.SetTotalPartsOfUploadRequest;
import de.consol.dus.s4.services.rest.api.boundary.rest.request.StartUploadRequest;
import de.consol.dus.s4.services.rest.api.boundary.rest.response.UploadResponse;
import de.consol.dus.s4.services.rest.api.usecases.api.exceptions.PartNumberAlreadyExistsException;
import de.consol.dus.s4.services.rest.api.usecases.api.exceptions.PartNumberIsBiggerThanTotalParts;
import de.consol.dus.s4.services.rest.api.usecases.api.exceptions.TotalPartsAlreadySetException;
import de.consol.dus.s4.services.rest.api.usecases.api.exceptions.TotalPartsSmallerThanMaxPartNumberException;
import de.consol.dus.s4.services.rest.api.usecases.api.requests.DeleteUploadByIdRequest;
import de.consol.dus.s4.services.rest.api.usecases.api.requests.GetAllUploadsRequest;
import de.consol.dus.s4.services.rest.api.usecases.api.responses.Upload;
import de.consol.dus.s4.services.rest.api.usecases.api.responses.UploadPart;
import io.opentelemetry.api.trace.Span;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.smallrye.mutiny.unchecked.Unchecked;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.reactive.MultipartForm;
import org.slf4j.MDC;

@Path("uploads")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class UploadEndpoint {
  @Operation(summary = "Gets all uploads.", operationId = "getAllUploads")
  @APIResponse(ref = "uploadListOk")
  @APIResponse(ref = "ise")
  @GET
  public Uni<Response> getAllUploads(
      @Parameter(ref = RequestFilter.CORRELATION_ID_HEADER_KEY)
      @HeaderParam(RequestFilter.CORRELATION_ID_HEADER_KEY)
      String correlationId) {
    Span.current().setAttribute(
        RequestFilter.CORRELATION_ID_MDC_KEY,
        MDC.get(RequestFilter.CORRELATION_ID_MDC_KEY));
    // @formatter:off
    return Uni.createFrom().item(new GetAllUploadsRequestImpl(correlationId))
        .onItem()
            .transform(GetAllUploadsRequest::execute)
            .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
        .onItem()
            .transformToMulti(Multi.createFrom()::iterable)
        .collect()
            .asList()
        .onItem()
            .transform(items -> Response.ok(items).build());
    // @formatter:on
  }

  @Operation(
      summary = "Starts a new upload.",
      description = "This is the first request to start an upload. It contains no content, and "
          + "returns an id that must be used in subsequent calls to identify the upload.",
      operationId = "startUpload")
  @APIResponse(ref = "uploadOk")
  @APIResponse(ref = "badRequest")
  @APIResponse(ref = "ise")
  @POST
  public Uni<Response> startUpload(
      @Parameter(ref = RequestFilter.CORRELATION_ID_HEADER_KEY)
      @HeaderParam(RequestFilter.CORRELATION_ID_HEADER_KEY)
      @Valid
      @NotBlank
      String correlationId,

      @RequestBody(ref = "StartUploadRequest")
      @Valid
      @NotNull StartUploadRequest request) {
    Span.current().setAttribute(
        RequestFilter.CORRELATION_ID_MDC_KEY,
        MDC.get(RequestFilter.CORRELATION_ID_MDC_KEY));
    // @formatter:off
    return Uni
        .createFrom()
            .item(new CreateNewUploadRequestImpl(request.getFileName(), correlationId))
        .onItem()
            .transform(CreateNewUploadRequestImpl::execute)
            .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
        .onItem()
            .transform(UploadResponse::new)
        .onItem()
            .transform(Response::ok)
        .onItem()
            .transform(Response.ResponseBuilder::build);
    // @formatter:on
  }

  @Operation(
      summary = "Gets one upload.",
      description = "Gets information to one upload. If no upload with the given id exists, a `404`"
          + "error with a corresponding error message is returned.",
      operationId = "getUpload")
  @APIResponse(ref = "uploadOk")
  @APIResponse(ref = "notFound")
  @APIResponse(ref = "ise")
  @GET
  @Path("{id}")
  public Uni<Response> getUpload(
      @Parameter(ref = RequestFilter.CORRELATION_ID_HEADER_KEY)
      @HeaderParam(RequestFilter.CORRELATION_ID_HEADER_KEY)
      @Valid
      @NotBlank
      String correlationId,

      @Parameter(ref = "id")
      @PathParam("id")
      long id) throws NoSuchEntityException {
    Span.current().setAttribute(
        RequestFilter.CORRELATION_ID_MDC_KEY,
        MDC.get(RequestFilter.CORRELATION_ID_MDC_KEY));
    // @formatter:off
    return Uni.createFrom().item(new GetUploadByIdRequestImpl(id, correlationId))
        .onItem()
            .transform(this::executeAndHandleFailures)
            .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
        .onItem()
            .transform(UploadResponse::new)
        .onItem()
            .transform(Response::ok)
        .onItem()
            .transform(Response.ResponseBuilder::build);
    // @formatter:on
  }

  private Upload executeAndHandleFailures(GetUploadByIdRequestImpl request) {
    return request.execute()
        .orElseThrow(() -> new NoSuchEntityException(Upload.class, request.getId()));
  }

  private Upload executeAndHandleFailures(SetTotalPartsForUploadRequestImpl request) {
    try {
      return request.execute()
          .orElseThrow(() -> new NoSuchEntityException(Upload.class, request.getId()));
    } catch (TotalPartsSmallerThanMaxPartNumberException e) {
      throw new ConflictException(
          "largest part number is already %d".formatted(e.getMaxPartNumber()),
          e);
    } catch (TotalPartsAlreadySetException e) {
      throw new ConflictException("totalParts already set", e);
    }
  }


  private Upload executeAndHandleFailures(AddPartToUploadRequestImpl request) {
    try {
      return request.execute()
          .orElseThrow(() -> new NoSuchEntityException(Upload.class, request.getId()));
    } catch (PartNumberAlreadyExistsException e) {
      throw new ConflictException(
          "part with number %d already exists".formatted(request.getPartNumber()),
          e);
    } catch (PartNumberIsBiggerThanTotalParts e) {
      throw new ConflictException(
          "totalParts for Upload set to %d, thus a part with number %d cannot be added".formatted(
              e.getTotalParts(),
              e.getPartNumber()),
          e);
    }
  }

  @Operation(
      summary = "Deletes an upload.",
      description = "Deletes one upload. This includes possibly all parts of the upload, if the "
          + "upload is still in progress. This endpoint should never return `404` error, even if "
          + "one tries to delete a non-existing upload.",
      operationId = "deleteUpload")
  @APIResponse(ref = "noContent")
  @APIResponse(ref = "ise")
  @DELETE
  @Path("{id}")
  public Uni<Response> deleteUpload(
      @Parameter(ref = RequestFilter.CORRELATION_ID_HEADER_KEY)
      @HeaderParam(RequestFilter.CORRELATION_ID_HEADER_KEY)
      @Valid
      @NotBlank
      String correlationId,

      @Parameter(ref = "id")
      @PathParam("id")
      long id) {
    Span.current().setAttribute(
        RequestFilter.CORRELATION_ID_MDC_KEY,
        MDC.get(RequestFilter.CORRELATION_ID_MDC_KEY));
    // @formatter:off
    return Uni.createFrom().item(new DeleteUploadByIdRequestImpl(id, correlationId))
        .onItem()
            .invoke(DeleteUploadByIdRequest::execute)
            .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
        .onItem()
            .transform(item -> Response.noContent())
        .onItem()
            .transform(Response.ResponseBuilder::build);
    // @formatter:on
  }

  @Operation(
      summary = "Sets the totalParts of one upload.",
      description = "Setting the totalParts has certain consequences. For one, it can only be set "
          + "once. For another, if the totalParts is set, no part with `partNumber > totalParts` "
          + "can be sent In this case, a `409` error with a corresponding error message is "
          + "returned. If already a part with `partNumber > totalParts` exists, a `409` error with"
          + "a corresponding error message is returned. If no upload with the given id exists, a "
          + "`404` error with a corresponding error message is returned.",
      operationId = "setTotalPartsForUpload")
  @APIResponse(ref = "uploadOk")
  @APIResponse(ref = "badRequest")
  @APIResponse(ref = "notFound")
  @APIResponse(ref = "conflict")
  @APIResponse(ref = "ise")
  @PUT
  @Path(value = "{id}/totalParts")
  public Uni<Response> addTotalParts(
      @Parameter(ref = RequestFilter.CORRELATION_ID_HEADER_KEY)
      @HeaderParam(RequestFilter.CORRELATION_ID_HEADER_KEY)
      @Valid
      @NotBlank
      String correlationId,

      @Parameter(ref = "id")
      @PathParam("id")
      long id,

      @RequestBody(ref = "SetTotalPartsOfUploadRequest")
      @Valid
      @NotNull
      SetTotalPartsOfUploadRequest request)
      throws NoSuchEntityException, ConflictException {
    Span.current().setAttribute(
        RequestFilter.CORRELATION_ID_MDC_KEY,
        MDC.get(RequestFilter.CORRELATION_ID_MDC_KEY));
    // @formatter:off
    return Uni
        .createFrom()
            .item(new SetTotalPartsForUploadRequestImpl(id, request.getTotalParts(), correlationId))
        .onItem()
            .transform(this::executeAndHandleFailures)
            .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
        .onItem()
            .transform(UploadResponse::new)
        .onItem()
            .transform(Response::ok)
        .onItem()
            .transform(Response.ResponseBuilder::build);
    // @formatter:on
  }

  @Operation(
      summary = "Gets all parts of an upload.",
      description = "Gets a list of all part number to an upload. If no upload with the given id "
          + "exists, a `404` error with a corresponding error message is returned.",
      operationId = "getPartsOfUpload")
  @APIResponse(ref = "uploadPartsListOk")
  @APIResponse(ref = "notFound")
  @APIResponse(ref = "ise")
  @GET
  @Path(value = "{id}/parts")
  public Uni<Response> getParts(
      @Parameter(ref = RequestFilter.CORRELATION_ID_HEADER_KEY)
      @HeaderParam(RequestFilter.CORRELATION_ID_HEADER_KEY)
      @Valid
      @NotBlank
      String correlationId,

      @Parameter(ref = "id")
      @PathParam("id")
      long id) {
    Span.current().setAttribute(
        RequestFilter.CORRELATION_ID_MDC_KEY,
        MDC.get(RequestFilter.CORRELATION_ID_MDC_KEY));
    // @formatter:off
    return Uni.createFrom().item(new GetUploadByIdRequestImpl(id, correlationId))
        .onItem()
            .transform(this::executeAndHandleFailures)
            .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
        .onItem()
            .transform(Upload::getParts)
        .onItem()
            .transformToMulti(Multi.createFrom()::iterable)
        .onItem()
          .transform(UploadPart::getPartNumber)
        .collect()
            .asList()
        .onItem()
            .transform(Response::ok)
        .onItem()
            .transform(Response.ResponseBuilder::build);
    // @formatter:on
  }

  @Operation(
      summary = "Adds a part to an upload.",
      description = "Adds a part (as octet-stream) to an upload. If the partNumber is larger than "
          + "totalParts, a `409` error with a corresponding error message is returned. If no "
          + "upload with the given id exists, a `404` error with a corresponding error message is "
          + "returned.",
      operationId = "addPartToUpload")
  @APIResponse(ref = "uploadOk")
  @APIResponse(ref = "badRequest")
  @APIResponse(ref = "notFound")
  @APIResponse(ref = "ise")
  @POST
  @Path(value = "{id}/parts")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Uni<Response> addPart(
      @Parameter(ref = RequestFilter.CORRELATION_ID_HEADER_KEY)
      @HeaderParam(RequestFilter.CORRELATION_ID_HEADER_KEY)
      @Valid
      @NotBlank
      String correlationId,

      @Parameter(ref = "id")
      @PathParam("id")
      long id,

      @RequestBody(ref = "AddPartToUploadRequest")
      @MultipartForm
      @Valid
      @NotNull
      AddPartToUploadRequest request) {
    Span.current().setAttribute(
        RequestFilter.CORRELATION_ID_MDC_KEY,
        MDC.get(RequestFilter.CORRELATION_ID_MDC_KEY));
    // @formatter:off
    return Uni.createFrom().voidItem()
        .onItem()
            .transform(Unchecked.function(nothing -> new AddPartToUploadRequestImpl(
                id,
                request.getPartNumber(),
                Files.readAllBytes(request.getContent().filePath()),
                correlationId)))
            .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
        .onItem()
            .transform(this::executeAndHandleFailures)
            .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
        .onItem()
            .transform(UploadResponse::new)
        .onItem()
            .transform(Response::ok)
        .onItem()
            .transform(Response.ResponseBuilder::build);
    // @formatter:on
  }

  @Operation(
      summary = "Gets the content of a port of an upload.",
      description = "The content is returned as octet stream, and the Content-Disposition header "
          + "is set to download the file. The file will be the name of the Upload (including file "
          + "extension) + `.part_XXXXXXXXXX`, where the `X`es represent the partNumber (possibly"
          + "containing leading Zeros). If no upload with the given id exists, a `404` error with "
          + "a corresponding error message is returned. If an upload with the given id exists, but "
          + "no part with the given partNumber exists, a `404` error with a corresponding error "
          + "message is returned.",
      operationId = "getContentOfPartOfUpload")
  @APIResponse(ref = "downloadOk")
  @APIResponse(ref = "badRequest")
  @APIResponse(ref = "notFound")
  @APIResponse(ref = "ise")
  @GET
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  @Path(value = "{id}/parts/{partNumber}/content")
  public Uni<Response> getPartContent(
      @Parameter(ref = RequestFilter.CORRELATION_ID_HEADER_KEY)
      @HeaderParam(RequestFilter.CORRELATION_ID_HEADER_KEY)
      @Valid
      @NotBlank
      String correlationId,

      @Parameter(ref = "id")
      @PathParam("id")
      long id,

      @Parameter(ref = "partNumber")
      @PathParam("partNumber")
      @Valid @Min(value = 1)
      int partNumber) {
    Span.current().setAttribute(
        RequestFilter.CORRELATION_ID_MDC_KEY,
        MDC.get(RequestFilter.CORRELATION_ID_MDC_KEY));
    // @formatter:off
    return Uni.createFrom().item(new GetUploadByIdRequestImpl(id, correlationId))
        .onItem()
            .transform(this::executeAndHandleFailures)
            .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
        .onItem()
            .transform(Upload::getParts)
        .onItem()
            .transformToMulti(Multi.createFrom()::iterable)
        .select()
            .where(part -> Objects.equals(part.getPartNumber(), partNumber))
        .select()
            .first()
            .toUni()
        .onItem().ifNull()
            .failWith(new NoSuchEntityException(UploadPart.class, id))
        .onItem()
            .transform(UploadPart::getContent)
        .onItem()
            .transform(content -> Response
                .ok(content)
                .header(
                    HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"%s.part_%010d\"".formatted(
                        new GetUploadByIdRequestImpl(id, correlationId)
                            .execute()
                            .orElseThrow(() -> new NoSuchEntityException(Upload.class, id))
                            .getFileName(),
                        partNumber))
                .build());
    // @formatter:on
  }

  @Operation(
      summary = "Gets the content of an upload.",
      description = "The content is returned as octet stream, and the Content-Disposition header "
          + "is set to download the file. The file will be the name of the Upload. If the upload"
          + "has not yet been finished, the endpoint returns a `204` (no content) response. If no "
          + "upload with the given id exists, a `404` error with a corresponding error message is "
          + "returned.",
      operationId = "getContentOfUpload")
  @APIResponse(ref = "downloadOk")
  @APIResponse(ref = "noContent")
  @APIResponse(ref = "notFound")
  @APIResponse(ref = "ise")
  @GET
  @Produces({MediaType.APPLICATION_OCTET_STREAM})
  @Path("{id}/content")
  public Uni<Response> getUploadContent(
      @Parameter(ref = RequestFilter.CORRELATION_ID_HEADER_KEY)
      @HeaderParam(RequestFilter.CORRELATION_ID_HEADER_KEY)
      @Valid
      @NotBlank
      String correlationId,

      @Parameter(ref = "id")
      @PathParam("id")
      long id) {
    Span.current().setAttribute(
        RequestFilter.CORRELATION_ID_MDC_KEY,
        MDC.get(RequestFilter.CORRELATION_ID_MDC_KEY));
    // @formatter:off
    return Uni.createFrom().item(new GetUploadByIdRequestImpl(id, correlationId))
        .onItem()
            .transform(this::executeAndHandleFailures)
            .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
        .onItem()
            .transform(result -> Optional.of(result)
                .map(Upload::getContent)
                .map(content -> Response
                    .ok(content)
                    .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"%s\"".formatted(result.getFileName())))
                .orElseGet(Response::noContent)
                .build());
    // @formatter:on
  }
}
