package de.consol.dus.s4.services.aggregator.boundary.dao;

import de.consol.dus.s4.services.aggregator.boundary.dao.entity.UploadEntity;
import de.consol.dus.s4.services.aggregator.boundary.dao.integration.usecases.responses.UploadImpl;
import de.consol.dus.s4.services.aggregator.usecases.spi.dao.UploadDao;
import de.consol.dus.s4.services.aggregator.usecases.spi.dao.requests.WriteContentAndReleasePartsRequest;
import de.consol.dus.s4.services.aggregator.usecases.spi.dao.responses.Upload;
import de.consol.dus.s4.services.aggregator.usecases.spi.dao.responses.UploadStatus;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;

@ApplicationScoped
@AllArgsConstructor
public class UploadDaoImpl implements UploadDao {
  private final UploadRepository repository;
  private final Logger logger;

  @WithSpan
  @Transactional
  @Override
  public Optional<Upload> getById(long id) {
    return repository.findById(id).map(UploadImpl::new);
  }

  @WithSpan
  @Transactional
  @Override
  public void writeContentAndDeletePartsAndSetStatusToDone(
      WriteContentAndReleasePartsRequest request) {
    final Optional<UploadEntity> maybeUpload = repository.findById(request.getId());
    if (maybeUpload.isEmpty()) {
      logger.info("Upload with id {} does not exist", request.getId());
      return;
    }
    final UploadEntity upload = maybeUpload.get().setContent(request.getContent());
    upload.getParts().clear();
    upload.setStatus(UploadStatus.DONE);
  }
}
