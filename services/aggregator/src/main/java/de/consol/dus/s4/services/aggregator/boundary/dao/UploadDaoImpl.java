package de.consol.dus.s4.services.aggregator.boundary.dao;

import de.consol.dus.s4.services.aggregator.boundary.dao.entity.UploadEntity;
import de.consol.dus.s4.services.aggregator.boundary.dao.integration.usecases.responses.UploadImpl;
import de.consol.dus.s4.services.aggregator.usecases.spi.dao.UploadDao;
import de.consol.dus.s4.services.aggregator.usecases.spi.dao.requests.GetUploadByIdRequest;
import de.consol.dus.s4.services.aggregator.usecases.spi.dao.requests.WriteContentAndReleasePartsRequest;
import de.consol.dus.s4.services.aggregator.usecases.spi.dao.responses.Upload;
import de.consol.dus.s4.services.aggregator.usecases.spi.dao.responses.UploadStatus;
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

  @Transactional
  @Override
  public Optional<Upload> getById(GetUploadByIdRequest request) {
    return repository.findById(request.getId()).map(UploadImpl::new);
  }

  @Transactional
  @Override
  public void writeContentAndDeletePartsAndSetStatusToDone(
      WriteContentAndReleasePartsRequest request) {
    final Optional<UploadEntity> maybeUpload = repository.findById(request.getId());
    if (maybeUpload.isEmpty()) {
      logger.warn("Request {}: Upload with id {} does not exist", request, request.getId());
      return;
    }
    final UploadEntity upload = maybeUpload.get().setContent(request.getContent());
    upload.getParts().clear();
    upload.setStatus(UploadStatus.DONE);
  }
}
