package de.consol.dus.s4.services.aggregator.boundary.dao.integration.usecases.responses;

import de.consol.dus.s4.services.aggregator.boundary.dao.entity.UploadEntity;
import de.consol.dus.s4.services.aggregator.usecases.spi.dao.responses.Upload;
import de.consol.dus.s4.services.aggregator.usecases.spi.dao.responses.UploadPart;
import de.consol.dus.s4.services.aggregator.usecases.spi.dao.responses.UploadStatus;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class UploadImpl implements Upload {
  @Getter(AccessLevel.PRIVATE)
  private final UploadEntity delegate;

  public long getId() {
    return getDelegate().getId();
  }

  public String getFileName() {
    return getDelegate().getFileName();
  }

  public List<UploadPart> getParts() {
    return Optional.of(getDelegate())
        .map(UploadEntity::getParts)
        .stream()
        .flatMap(List::stream)
        .map(UploadPartImpl::new)
        .map(UploadPart.class::cast)
        .toList();
  }

  public UploadStatus getStatus() {
    return getDelegate().getStatus();
  }

  public Integer getTotalParts() {
    return getDelegate().getTotalParts();
  }

  public byte[] getContent() {
    return getDelegate().getContent();
  }
}
