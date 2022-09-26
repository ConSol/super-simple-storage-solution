package de.consol.dus.s4.services.aggregator.usecases.spi.dao.responses;

import java.util.List;

@SuppressWarnings("unused")
public interface Upload {
  long getId();

  String getFileName();

  List<UploadPart> getParts();

  UploadStatus getStatus();

  Integer getTotalParts();

  byte[] getContent();
}
