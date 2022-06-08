package de.consol.dus.s4.services.rest.api.boundary.rest.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.MediaType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddPartToUploadRequest {
  @RestForm("partNumber")
  @PartType(MediaType.TEXT_PLAIN)
  @NotNull
  @Min(value = 1)
  Integer partNumber;

  @RestForm("content")
  @PartType(MediaType.APPLICATION_OCTET_STREAM)
  @Schema(type = SchemaType.STRING, format = "binary")
  @NotNull
  FileUpload content;

  // These two setters are necessary so that quarkus can initialize the multipart fields
  @SuppressWarnings("unused")
  public void setPartNumber(Integer partNumber) {
    this.partNumber = partNumber;
  }

  @SuppressWarnings("unused")
  public void setContent(FileUpload content) {
    this.content = content;
  }
}
