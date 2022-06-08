package de.consol.dus.s4.services.rest.api.boundary.rest.openapi;

import de.consol.dus.s4.commons.correlation.http.exceptions.filter.container.RequestFilter;
import de.consol.dus.s4.commons.http.exceptions.response.ErrorResponse;
import de.consol.dus.s4.services.rest.api.boundary.rest.request.AddPartToUploadRequest;
import de.consol.dus.s4.services.rest.api.boundary.rest.request.SetTotalPartsOfUploadRequest;
import de.consol.dus.s4.services.rest.api.boundary.rest.request.StartUploadRequest;
import de.consol.dus.s4.services.rest.api.boundary.rest.response.UploadResponse;
import de.consol.dus.s4.services.rest.api.usecases.api.responses.UploadStatus;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Components;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.reactive.multipart.FileUpload;

@OpenAPIDefinition(
    info = @Info(
        title = "title",
        version = "version"),
    components = @Components(
        headers = {
            @Header(
                name = RequestFilter.CORRELATION_ID_HEADER_KEY,
                description = "X-Correlation-ID header, for request tracing, see [this link "
                    + "(Wikipedia)]"
                    + "(https://en.wikipedia.org/wiki/List_of_HTTP_header_fields#Common_non-standard_response_fields) "
                    + "for more information. Clients are free to send this header. If they do not "
                    + "send them, the backend will generate this header."),
            @Header(
                name = HttpHeaders.CONTENT_DISPOSITION,
                description = "The Content-Disposition header. For more information, see [here "
                    + "(Mozilla)]"
                    + "(https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Disposition).")},
        schemas = {
            @Schema(
                name = "uploadId",
                title = "the id uniquely identifying an upload",
                description = "the id uniquely identifying an upload",
                type = SchemaType.INTEGER,
                format = "int64",
                required = true),
            @Schema(
                name = "partNumber",
                title = "the number of an upload part",
                description = "the number of an upload part",
                type = SchemaType.INTEGER,
                format = "int32",
                required = true,
                minimum = "1"),
            @Schema(
                name = "content",
                title = "The content of an upload or upload part",
                description = "The content of an upload or upload part",
                type = SchemaType.STRING,
                format = "binary",
                required = true),
            @Schema(
                name = "StartUploadRequest",
                title = "Starts a new upload",
                description = "This schema describes the body sent to start a new upload.",
                implementation = StartUploadRequest.class,
                example = """
                    {
                      "fileName": "Image.png"
                    }
                    """),
            @Schema(
                name = "AddPartToUploadRequest",
                title = "Adds a part to an upload",
                description = "This schema is sent to add a part with the given `partNumber` and "
                    + "content to a previously started upload",
                required = true,
                implementation = AddPartToUploadRequest.class,
                properties = {
                    @SchemaProperty(name = "partNumber", ref = "partNumber"),
                    @SchemaProperty(name = "content", ref = "content")},
                requiredProperties = {"partNumber", "content"}),
            @Schema(
                name = "SetTotalPartsOfUploadRequest",
                title = "Set `totalParts` of an upload",
                description = "The schema is sent to set the `totalParts` of an upload.",
                implementation = SetTotalPartsOfUploadRequest.class),
            @Schema(
                name = "UploadResponse",
                title = "Upload response schema",
                description = "This schema describes the return value for all requests that "
                    + "directly deal with uploads (not parts, though).",
                implementation = UploadResponse.class,
                example = """
                    {
                      "id": 1,
                      "fileName": "Image.png",
                      "status": "UPLOAD_IN_PROGRESS",
                      "partsReceived": 0,
                      "totalParts": null
                    }
                    """),
            @Schema(
                name = "UploadStatus",
                title = "Status of the upload",
                implementation = UploadStatus.class),
            @Schema(
                name = "ErrorResponse",
                title = "Error Response Schema",
                description = "This schema is for general error responses.",
                implementation = ErrorResponse.class),
            @Schema(
                name = "FileUpload",
                title = "Binary Data for a part upload",
                description = "This schema represents the binary data of a part upload.",
                implementation = FileUpload.class)},
        parameters = {
            @Parameter(
                name = "id",
                description = "The id uniquely identifying an upload",
                in = ParameterIn.PATH,
                required = true,
                schema = @Schema(ref = "uploadId")),
            @Parameter(
                name = "partNumber",
                description = "The `partNumber`, uniquely identifying a part of an upload.",
                in = ParameterIn.PATH,
                required = true,
                schema = @Schema(ref = "partNumber")),
            @Parameter(
                name = RequestFilter.CORRELATION_ID_HEADER_KEY,
                description = "X-Correlation-ID header, for request tracing, see [this link "
                    + "(Wikipedia)]"
                    + "(https://en.wikipedia.org/wiki/List_of_HTTP_header_fields#Common_non-standard_response_fields) "
                    + "for more information. If the client does not sent this header, the backend "
                    + "will generate one in form of an v4 UUID.",
                in = ParameterIn.HEADER,
                schema = @Schema(implementation = String.class, type = SchemaType.STRING))},
        requestBodies = {
            @RequestBody(
                name = "StartUploadRequest",
                description = "The body sent to start an upload.",
                required = true,
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(ref = "StartUploadRequest"))),
            @RequestBody(
                name = "AddPartToUploadRequest",
                description = "The body sent to add a part to an upload.",
                required = true,
                content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA,
                    schema = @Schema(ref = "AddPartToUploadRequest"))),
            @RequestBody(
                name = "SetTotalPartsOfUploadRequest",
                description = "The body sent to set the `totalParts` of an upload",
                required = true,
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(ref = "SetTotalPartsOfUploadRequest")))},
        responses = {
            @APIResponse(
                name = "uploadOk",
                description = "The operation completed successfully.",
                responseCode = "200",
                headers = {@Header(ref = RequestFilter.CORRELATION_ID_HEADER_KEY)},
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(ref = "UploadResponse"))),
            @APIResponse(
                name = "uploadListOk",
                description = "The operation completed successfully.",
                responseCode = "200",
                headers = {@Header(ref = RequestFilter.CORRELATION_ID_HEADER_KEY)},
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(
                        type = SchemaType.ARRAY,
                        implementation = UploadResponse.class))),
            @APIResponse(
                name = "downloadOk",
                responseCode = "200",
                description = "The operation completed successfully.",
                headers = {
                    @Header(ref = RequestFilter.CORRELATION_ID_HEADER_KEY),
                    @Header(ref = HttpHeaders.CONTENT_DISPOSITION)},
                content = @Content(
                    mediaType = MediaType.APPLICATION_OCTET_STREAM,
                    schema = @Schema(ref = "content"))),
            @APIResponse(
                name = "uploadPartsListOk",
                responseCode = "200",
                description = "The operation completed successfully.",
                headers = {@Header(ref = RequestFilter.CORRELATION_ID_HEADER_KEY)},
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.ARRAY, implementation = Integer.class))),
            @APIResponse(
                name = "noContent",
                responseCode = "204",
                description = "The operation completed successfully.",
                headers = {@Header(ref = RequestFilter.CORRELATION_ID_HEADER_KEY)}),
            @APIResponse(
                name = "badRequest",
                responseCode = "400",
                description = "The request does not conform to the specification.",
                headers = {@Header(ref = RequestFilter.CORRELATION_ID_HEADER_KEY)},
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(ref = "ErrorResponse"))),
            @APIResponse(
                name = "notFound",
                responseCode = "404",
                description = "The requested entity was not found.",
                headers = {@Header(ref = RequestFilter.CORRELATION_ID_HEADER_KEY)},
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(ref = "ErrorResponse"))),
            @APIResponse(
                name = "conflict",
                responseCode = "409",
                description =
                    "There was a conflict between the data in the request and the data on the "
                        + "server. Server data was not modified. Submitting the same request "
                        + "without taking further actions will result in the same behaviour.",
                headers = {@Header(ref = RequestFilter.CORRELATION_ID_HEADER_KEY)},
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(ref = "ErrorResponse"))),
            @APIResponse(
                name = "ise",
                responseCode = "500",
                description = "An internal server error occurred. This is not the client's fault.",
                headers = {@Header(ref = RequestFilter.CORRELATION_ID_HEADER_KEY)},
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(ref = "ErrorResponse")))}))
@SuppressWarnings("unused")
public class Definition extends Application {
}

