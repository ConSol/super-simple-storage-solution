package de.consol.dus.s4.services.aggregator.usecases;

import de.consol.dus.s4.services.aggregator.usecases.spi.dao.UploadDao;
import de.consol.dus.s4.services.aggregator.usecases.spi.dao.requests.WriteContentAndReleasePartsRequest;
import de.consol.dus.s4.services.aggregator.usecases.spi.dao.responses.Upload;
import de.consol.dus.s4.services.aggregator.usecases.spi.dao.responses.UploadPart;
import de.consol.dus.s4.services.aggregator.usecases.spi.dao.responses.UploadStatus;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;

@ApplicationScoped
@RequiredArgsConstructor
public class AggregateUploadDataUseCase {
  private final UploadDao dao;
  private final Logger logger;

  @WithSpan
  public void execute(long id) {
    logger.info("Received request to aggregate parts of Upload with id {}", id);
    final Optional<Upload> maybeUpload = dao.getById(id);
    if (maybeUpload.isEmpty()) {
      logger.info("Upload with id {} does not exist", id);
      return;
    }
    final Upload upload = maybeUpload.get();
    if (isUploadReadyToBeProcessed(upload)) {
      final byte[] content = aggregateContent(upload);
      dao.writeContentAndDeletePartsAndSetStatusToDone(new WriteContentAndReleasePartsRequest(
          id,
          content));
    }
  }

  private static byte[] aggregateContent(Upload upload) {
    final List<byte[]> sortedContentParts = upload.getParts().stream()
        .sorted(Comparator.comparing(UploadPart::getPartNumber))
        .map(UploadPart::getContent)
        .toList();
    final int totalContentSize = sortedContentParts.stream()
        .map(Array::getLength)
        .mapToInt(Integer::intValue)
        .sum();
    final byte[] content = new byte[totalContentSize];
    int offset = 0;
    for (byte[] contentPart : sortedContentParts) {
      final int contentPartLength = contentPart.length;
      System.arraycopy(contentPart, 0, content, offset, contentPartLength);
      offset += contentPartLength;
    }
    return content;
  }

  private boolean isUploadReadyToBeProcessed(Upload upload) {
    if (isStillInProgress(upload)) {
      return false;
    }
    if (isAlreadyDone(upload)) {
      return false;
    }
    if (hasNoTotalSize(upload)) {
      return false;
    }
    return allPartsReceived(upload);
  }

  private boolean isStillInProgress(Upload upload) {
    if (Objects.equals(upload.getStatus(), UploadStatus.UPLOAD_IN_PROGRESS)) {
      logger.debug("Upload with id {} is still in progress", upload.getId());
      return true;
    }
    return false;
  }

  private boolean isAlreadyDone(Upload upload) {
    if (Objects.equals(upload.getStatus(), UploadStatus.DONE)) {
      logger.debug("Upload with id {} is already done", upload.getId());
      return true;
    }
    return false;
  }

  private boolean hasNoTotalSize(Upload upload) {
    if (Objects.isNull(upload.getTotalParts())) {
      logger.debug("Upload with id {} has totalParts not yet set", upload.getId());
      return true;
    }
    return false;
  }

  private boolean allPartsReceived(Upload upload) {
    final int receivedParts = upload.getParts().size();
    final int totalParts = upload.getTotalParts();
    if (receivedParts != totalParts) {
      logger.debug(
          "Upload with id {} has totalParts set to {}, but only {} received until now",
          upload.getId(),
          totalParts,
          receivedParts);
      return false;
    }
    return true;
  }
}
