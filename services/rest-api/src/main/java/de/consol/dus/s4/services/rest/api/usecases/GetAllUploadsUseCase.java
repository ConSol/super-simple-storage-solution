package de.consol.dus.s4.services.rest.api.usecases;

import de.consol.dus.s4.services.rest.api.usecases.api.exceptions.SingletonNotInitializedError;
import de.consol.dus.s4.services.rest.api.usecases.api.responses.Upload;
import de.consol.dus.s4.services.rest.api.usecases.spi.dao.UploadDao;
import de.consol.dus.s4.services.rest.api.usecases.spi.dao.requests.GetAllUploadsRequest;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GetAllUploadsUseCase {
  private static final AtomicReference<GetAllUploadsUseCase> INSTANCE = new AtomicReference<>();

  private final UploadDao dao;
  private final Logger logger;

  public Collection<Upload> execute(GetAllUploadsRequest request) {
    logger.info("Received request: {}", request);
    return dao.getAll(request).stream()
        .map(Upload.class::cast)
        .toList();
  }

  public static GetAllUploadsUseCase getInitializedInstance(String correlationId) {
    try {
      return getInstance(null, null);
    } catch (IllegalStateException cause) {
      throw new SingletonNotInitializedError(cause, correlationId);
    }
  }

  public static GetAllUploadsUseCase getInstance(UploadDao dao, Logger logger) {
    if (Objects.isNull(INSTANCE.get())) {
      synchronized (GetAllUploadsUseCase.class) {
        if (Objects.isNull(INSTANCE.get())) {
          if (Objects.isNull(dao) || Objects.isNull(logger)) {
            throw new IllegalStateException(
                "nulls are only allowed when the singleton is initialized. The singleton is not "
                    + " yet initialized. Please call static method "
                    + "GetAllUploadsUseCase.getInstance(UploadDao, Logger) with non-null "
                    + "parameters.");
          }
          INSTANCE.set(new GetAllUploadsUseCase(dao, logger));
        }
      }
    }
    return INSTANCE.get();
  }
}
