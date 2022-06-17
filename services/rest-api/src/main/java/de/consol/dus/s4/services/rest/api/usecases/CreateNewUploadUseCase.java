package de.consol.dus.s4.services.rest.api.usecases;

import de.consol.dus.s4.services.rest.api.usecases.api.exceptions.SingletonNotInitializedError;
import de.consol.dus.s4.services.rest.api.usecases.api.requests.CreateNewUploadRequest;
import de.consol.dus.s4.services.rest.api.usecases.api.responses.Upload;
import de.consol.dus.s4.services.rest.api.usecases.spi.dao.UploadDao;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.opentracing.Traced;
import org.slf4j.Logger;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateNewUploadUseCase {
  private static final AtomicReference<CreateNewUploadUseCase> INSTANCE = new AtomicReference<>();

  private final UploadDao dao;
  private final Logger logger;

  @Traced
  public Upload execute(CreateNewUploadRequest request) {
    logger.warn("Received request: {}", request);
    return dao.createNewUpload(request);
  }

  public static CreateNewUploadUseCase getInitializedInstance(String correlationId) {
    try {
      return getInstance(null, null);
    } catch (IllegalStateException cause) {
      throw new SingletonNotInitializedError(cause, correlationId);
    }
  }

  public static CreateNewUploadUseCase getInstance(UploadDao dao, Logger logger) {
    if (Objects.isNull(INSTANCE.get())) {
      synchronized (CreateNewUploadUseCase.class) {
        if (Objects.isNull(INSTANCE.get())) {
          if (Objects.isNull(dao) || Objects.isNull(logger)) {
            throw new IllegalStateException(
                "nulls are only allowed when the singleton is initialized. The singleton is not "
                    + " yet initialized. Please call static method "
                    + "CreateNewUploadUseCase.getInstance(UploadDao, Logger) with non-null "
                    + "parameters.");
          }
          INSTANCE.set(new CreateNewUploadUseCase(dao, logger));
        }
      }
    }
    return INSTANCE.get();
  }
}
