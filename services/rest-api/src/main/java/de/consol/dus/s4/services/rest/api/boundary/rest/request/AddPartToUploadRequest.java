package de.consol.dus.s4.services.rest.api.boundary.rest.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

@SuppressWarnings("unused")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddPartToUploadRequest {
  @RestForm("partNumber")
  @PartType(MediaType.TEXT_PLAIN)
  @NotNull
  @Min(value = 1)
  private Integer partNumber;

  @RestForm("content")
  @PartType(MediaType.APPLICATION_OCTET_STREAM)
  @Schema(type = SchemaType.STRING, format = "binary")
  @NotNull
  private FileUpload content;

  // These two setters are necessary so that quarkus can initialize the multipart fields
  public void setPartNumber(Integer partNumber) {
    this.partNumber = partNumber;
  }

  public void setContent(FileUpload content) {
    this.content = content;
  }
}
