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
import de.consol.dus.s4.services.rest.api.usecases.api.responses.Upload;
import de.consol.dus.s4.services.rest.api.usecases.api.responses.UploadPart;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Objects;
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

@Path("uploads")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class UploadEndpoint {
  @Operation(summary = "Gets all uploads.", operationId = "getAllUploads")
  @APIResponse(ref = "uploadListOk")
  @APIResponse(ref = "ise")
  @GET
  public Response getAllUploads(
      @Parameter(ref = RequestFilter.CORRELATION_ID_HEADER_KEY)
      @HeaderParam(RequestFilter.CORRELATION_ID_HEADER_KEY)
      String correlationId) {
    final Collection<Upload> results = new GetAllUploadsRequestImpl(correlationId).execute();
    return Response
        .ok(results.stream()
            .map(UploadResponse::new)
            .toList())
        .build();
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
  public Response startUpload(
      @Parameter(ref = RequestFilter.CORRELATION_ID_HEADER_KEY)
      @HeaderParam(RequestFilter.CORRELATION_ID_HEADER_KEY)
      @Valid
      @NotBlank
      String correlationId,

      @RequestBody(ref = "StartUploadRequest")
      @Valid
      @NotNull StartUploadRequest request) {
    final Upload result =
        new CreateNewUploadRequestImpl(request.getFileName(), correlationId).execute();
    return Response
        .ok(new UploadResponse(result))
        .build();
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
  public Response getUpload(
      @Parameter(ref = RequestFilter.CORRELATION_ID_HEADER_KEY)
      @HeaderParam(RequestFilter.CORRELATION_ID_HEADER_KEY)
      @Valid
      @NotBlank
      String correlationId,

      @Parameter(ref = "id")
      @PathParam("id")
      long id) throws NoSuchEntityException {
    final Upload result = new GetUploadByIdRequestImpl(id, correlationId).execute()
        .orElseThrow(() -> new NoSuchEntityException(Upload.class, id));
    return Response
        .ok(new UploadResponse(result))
        .build();
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
  public Response deleteUpload(
      @Parameter(ref = RequestFilter.CORRELATION_ID_HEADER_KEY)
      @HeaderParam(RequestFilter.CORRELATION_ID_HEADER_KEY)
      @Valid
      @NotBlank
      String correlationId,

      @Parameter(ref = "id")
      @PathParam("id")
      long id) {
    new DeleteUploadByIdRequestImpl(id, correlationId).execute();
    return Response.noContent().build();
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
  public Response addTotalParts(
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
    final Upload result;
    try {
      result = new SetTotalPartsForUploadRequestImpl(id, request.getTotalParts(), correlationId)
          .execute()
          .orElseThrow(() -> new NoSuchEntityException(Upload.class, id));
    } catch (TotalPartsSmallerThanMaxPartNumberException cause) {
      throw new ConflictException(
          "largest part number is already %d".formatted(cause.getMaxPartNumber()),
          cause);
    } catch (TotalPartsAlreadySetException cause) {
      throw new ConflictException("totalParts already set", cause);
    }
    return Response
        .ok(new UploadResponse(result))
        .build();
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
  public Response getParts(
      @Parameter(ref = RequestFilter.CORRELATION_ID_HEADER_KEY)
      @HeaderParam(RequestFilter.CORRELATION_ID_HEADER_KEY)
      @Valid
      @NotBlank
      String correlationId,

      @Parameter(ref = "id")
      @PathParam("id")
      long id) {
    return Response
        .ok(new GetUploadByIdRequestImpl(id, correlationId).execute()
            .map(Upload::getParts)
            .orElseThrow(() -> new NoSuchEntityException(Upload.class, id))
            .stream()
            .map(UploadPart::getPartNumber)
            .toList())
        .build();
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
  public Response addPart(
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
      AddPartToUploadRequest request)
      throws IOException, NoSuchEntityException, ConflictException {
    final Upload result;
    try {
      // @formatter:off
      result = new AddPartToUploadRequestImpl(
              id,
              request.getPartNumber(),
              Files.readAllBytes(request.getContent().filePath()),
              correlationId)
          .execute()
          .orElseThrow(() -> new NoSuchEntityException(Upload.class, id));
      // @formatter:on
    } catch (PartNumberAlreadyExistsException cause) {
      throw new ConflictException(
          "part with number %d already exists".formatted(request.getPartNumber()),
          cause
      );
    } catch (PartNumberIsBiggerThanTotalParts cause) {
      throw new ConflictException(
          "totalParts for Upload set to %d, thus a part with number %d cannot be added".formatted(
              cause.getTotalParts(),
              cause.getPartNumber()),
          cause);
    }
    return Response.ok(new UploadResponse(result)).build();
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
  public Response getPartContent(
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
    return Response
        .ok(new GetUploadByIdRequestImpl(id, correlationId).execute()
            .orElseThrow(() -> new NoSuchEntityException(Upload.class, id))
            .getParts().stream()
            .filter(part -> Objects.equals(part.getPartNumber(), partNumber))
            .map(UploadPart::getContent)
            .findFirst()
            .orElseThrow(() -> new NoSuchEntityException(
                "UploadPart with number %s does not exist".formatted(partNumber))))
        .header(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"%s.part_%010d\"".formatted(
                new GetUploadByIdRequestImpl(id, correlationId).execute()
                    .orElseThrow(() ->
                        new NoSuchEntityException(Upload.class, id)).getFileName(), partNumber))
        .build();
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
  public Response getUploadContent(
      @Parameter(ref = RequestFilter.CORRELATION_ID_HEADER_KEY)
      @HeaderParam(RequestFilter.CORRELATION_ID_HEADER_KEY)
      @Valid
      @NotBlank
      String correlationId,

      @Parameter(ref = "id")
      @PathParam("id")
      long id) {
    final Upload result = new GetUploadByIdRequestImpl(id, correlationId).execute()
        .orElseThrow(() -> new NoSuchEntityException(Upload.class, id));
    final byte[] content = result.getContent();
    if (Objects.nonNull(content)) {
      return Response
          .ok(content)
          .type(MediaType.APPLICATION_OCTET_STREAM)
          .header(
              HttpHeaders.CONTENT_DISPOSITION,
              "attachment; filename=\"%s\"".formatted(result.getFileName()))
          .build();
    }
    return Response.noContent().build();
  }
}
