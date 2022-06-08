package de.consol.dus.s4.services.rest.api.usecases.api.responses;

import java.util.List;

public interface Upload {
  long getId();

  String getFileName();

  List<? extends UploadPart> getParts();

  UploadStatus getStatus();

  Integer getTotalParts();

  byte[] getContent();
}
