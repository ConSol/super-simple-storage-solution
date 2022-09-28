package de.consol.dus.s4.services.rest.api.boundary.dao;

import de.consol.dus.s4.services.rest.api.boundary.dao.entity.UploadEntity;
import de.consol.dus.s4.services.rest.api.boundary.dao.entity.UploadPartEntity;
import de.consol.dus.s4.services.rest.api.boundary.dao.integration.usecases.responses.UploadImpl;
import de.consol.dus.s4.services.rest.api.usecases.api.exceptions.PartNumberAlreadyExistsException;
import de.consol.dus.s4.services.rest.api.usecases.api.exceptions.PartNumberIsBiggerThanTotalParts;
import de.consol.dus.s4.services.rest.api.usecases.spi.dao.UploadDao;
import de.consol.dus.s4.services.rest.api.usecases.spi.dao.requests.AddPartToUploadRequest;
import de.consol.dus.s4.services.rest.api.usecases.spi.dao.requests.CreateNewUploadRequest;
import de.consol.dus.s4.services.rest.api.usecases.spi.dao.requests.SetStatusOfUploadRequest;
import de.consol.dus.s4.services.rest.api.usecases.spi.dao.requests.SetTotalPartsForUploadRequest;
import de.consol.dus.s4.services.rest.api.usecases.spi.dao.responses.Upload;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class UploadDaoImpl implements UploadDao {
  private final UploadRepository repository;

  @WithSpan
  @Transactional
  @Override
  public Upload createNewUpload(CreateNewUploadRequest request) {
    return Optional.of(new UploadEntity().setFileName(request.fileName()))
        .map(repository::save)
        .map(UploadImpl::new)
        .orElseThrow();
  }

  @WithSpan
  @Transactional
  @Override
  public Optional<Upload> getById(long id) {
    return repository.findById(id).map(UploadImpl::new);
  }

  @WithSpan
  @Transactional
  @Override
  public void deleteById(long id) {
    try {
      repository.deleteById(id);
    } catch (Exception e) {
      // Suppress exceptions, they get logged anyway through the transaction
    }
  }

  @WithSpan
  @Override
  public Optional<Upload> addPartToUpload(AddPartToUploadRequest request)
      throws PartNumberAlreadyExistsException, PartNumberIsBiggerThanTotalParts {
    try {
      return fetchUploadAndAddPart(request);
    } catch (RuntimeException exception) {
      final boolean isConstraintViolationException = UploadDaoImpl.hasCauseContainingText(
          exception,
          UploadPartEntity.UNIQUE_CONSTRAINT_UPLOAD_PART_NAME);
      if (isConstraintViolationException) {
        throw new PartNumberAlreadyExistsException(
            request.id(),
            request.partNumber());
      }
      throw exception;
    }
  }

  @Transactional
  Optional<Upload> fetchUploadAndAddPart(AddPartToUploadRequest request)
      throws PartNumberIsBiggerThanTotalParts {
    final Optional<UploadEntity> maybeExisting = repository.findById(request.id());
    if (maybeExisting.isEmpty()) {
      return Optional.empty();
    }
    final UploadEntity existing = maybeExisting.get();
    final int totalParts =
        Optional.of(existing).map(UploadEntity::getTotalParts).orElse(Integer.MAX_VALUE);
    if (request.partNumber() > totalParts) {
      throw new PartNumberIsBiggerThanTotalParts(
          existing.getId(),
          totalParts,
          request.partNumber());
    }
    existing.getParts().add(new UploadPartEntity()
        .setUpload(existing)
        .setPartNumber(request.partNumber())
        .setContent(request.content()));
    return Optional.of(existing).map(UploadImpl::new);
  }

  private static boolean hasCauseContainingText(Throwable t, String text) {
    if (t.getMessage().contains(text)) {
      return true;
    }
    if (Objects.nonNull(t.getCause())) {
      return hasCauseContainingText(t.getCause(), text);
    }
    return false;
  }

  @WithSpan
  @Transactional
  @Override
  public Collection<Upload> getAll() {
    return repository.findAll().stream()
        .map(UploadImpl::new)
        .map(Upload.class::cast)
        .toList();
  }

  @WithSpan
  @Transactional
  @Override
  public Optional<Upload> setTotalPartsForUpload(SetTotalPartsForUploadRequest request) {
    return repository.findById(request.id())
        .map(upload -> upload.setTotalParts(request.totalParts()))
        .map(UploadImpl::new);
  }

  @WithSpan
  @Transactional
  @Override
  public void setStatusOfUpload(SetStatusOfUploadRequest request) {
    repository.findById(request.id())
        .ifPresent(entity -> entity.setStatus(request.status()));
  }
}
