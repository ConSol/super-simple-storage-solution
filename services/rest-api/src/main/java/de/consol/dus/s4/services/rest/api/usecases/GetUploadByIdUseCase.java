package de.consol.dus.s4.services.rest.api.usecases;

import de.consol.dus.s4.services.rest.api.usecases.api.exceptions.SingletonNotInitializedError;
import de.consol.dus.s4.services.rest.api.usecases.api.responses.Upload;
import de.consol.dus.s4.services.rest.api.usecases.spi.dao.UploadDao;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GetUploadByIdUseCase {
  private static final AtomicReference<GetUploadByIdUseCase> INSTANCE = new AtomicReference<>();

  private final UploadDao dao;
  private final Logger logger;

  public Optional<Upload> execute(long id) {
    logger.info("Received request to get upload with id{}", id);
    return dao.getById(id).map(Upload.class::cast);
  }

  public static GetUploadByIdUseCase getInitializedInstance() {
    try {
      return getInstance(null, null);
    } catch (IllegalStateException cause) {
      throw new SingletonNotInitializedError(cause);
    }
  }

  public static GetUploadByIdUseCase getInstance(UploadDao dao, Logger logger) {
    if (Objects.isNull(INSTANCE.get())) {
      synchronized (GetUploadByIdUseCase.class) {
        if (Objects.isNull(INSTANCE.get())) {
          if (Objects.isNull(dao) || Objects.isNull(logger)) {
            throw new IllegalStateException(
                "nulls are only allowed when the singleton is initialized. The singleton is not "
                    + " yet initialized. Please call static method "
                    + "GetUploadByIdUseCase.getInstance(UploadDao, Logger) with non-null "
                    + "parameters.");
          }
          INSTANCE.set(new GetUploadByIdUseCase(dao, logger));
        }
      }
    }
    return INSTANCE.get();
  }
}
