package de.consol.dus.s4.services.rest.api.usecases;

import de.consol.dus.s4.services.rest.api.usecases.api.exceptions.SingletonNotInitializedError;
import de.consol.dus.s4.services.rest.api.usecases.api.requests.DeleteUploadByIdRequest;
import de.consol.dus.s4.services.rest.api.usecases.spi.dao.UploadDao;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.opentracing.Traced;
import org.slf4j.Logger;

@RequiredArgsConstructor
public class DeleteUploadByIdUseCase {
  private static final AtomicReference<DeleteUploadByIdUseCase> INSTANCE = new AtomicReference<>();

  private final UploadDao dao;
  private final Logger logger;

  @Traced
  public void execute(DeleteUploadByIdRequest request) {
    logger.info("Received request: {}", request);
    dao.deleteById(request);
  }

  public static DeleteUploadByIdUseCase getInitializedInstance(String correlationId) {
    try {
      return getInstance(null, null);
    } catch (IllegalStateException cause) {
      throw new SingletonNotInitializedError(cause, correlationId);
    }
  }

  public static DeleteUploadByIdUseCase getInstance(UploadDao dao, Logger logger) {
    if (Objects.isNull(INSTANCE.get())) {
      synchronized (DeleteUploadByIdUseCase.class) {
        if (Objects.isNull(INSTANCE.get())) {
          if (Objects.isNull(dao) || Objects.isNull(logger)) {
            throw new IllegalStateException(
                "nulls are only allowed when the singleton is initialized. The singleton is not "
                    + " yet initialized. Please call static method "
                    + "DeleteUploadByIdUseCase.getInstance(UploadDao, Logger) with non-null "
                    + "parameters.");
          }
          INSTANCE.set(new DeleteUploadByIdUseCase(dao, logger));
        }
      }
    }
    return INSTANCE.get();
  }
}
