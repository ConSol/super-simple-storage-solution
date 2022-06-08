package de.consol.dus.s4.services.aggregator.boundary.dao.integration.usecases.responses;

import de.consol.dus.s4.services.aggregator.boundary.dao.entity.UploadEntity;
import de.consol.dus.s4.services.aggregator.usecases.spi.dao.responses.Upload;
import de.consol.dus.s4.services.aggregator.usecases.spi.dao.responses.UploadPart;
import de.consol.dus.s4.services.aggregator.usecases.spi.dao.responses.UploadStatus;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class UploadImpl implements Upload {
  private final UploadEntity delegate;

  public long getId() {
    return this.delegate.getId();
  }

  public String getFileName() {
    return this.delegate.getFileName();
  }

  public List<UploadPart> getParts() {
    return Optional.of(this.delegate)
        .map(UploadEntity::getParts)
        .stream()
        .flatMap(List::stream)
        .map(UploadPartImpl::new)
        .map(UploadPart.class::cast)
        .toList();
  }

  public UploadStatus getStatus() {
    return this.delegate.getStatus();
  }

  public Integer getTotalParts() {
    return this.delegate.getTotalParts();
  }

  public byte[] getContent() {
    return this.delegate.getContent();
  }
}
