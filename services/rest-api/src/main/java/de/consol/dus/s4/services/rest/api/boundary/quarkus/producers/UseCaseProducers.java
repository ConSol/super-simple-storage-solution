package de.consol.dus.s4.services.rest.api.boundary.quarkus.producers;

import de.consol.dus.s4.commons.correlation.logging.logger.CorrelationLogger;
import de.consol.dus.s4.services.rest.api.usecases.AddPartToUploadUseCase;
import de.consol.dus.s4.services.rest.api.usecases.CreateNewUploadUseCase;
import de.consol.dus.s4.services.rest.api.usecases.DeleteUploadByIdUseCase;
import de.consol.dus.s4.services.rest.api.usecases.EnterProcessingStageUseCase;
import de.consol.dus.s4.services.rest.api.usecases.GetAllUploadsUseCase;
import de.consol.dus.s4.services.rest.api.usecases.GetUploadByIdUseCase;
import de.consol.dus.s4.services.rest.api.usecases.SetTotalPartsUseCase;
import de.consol.dus.s4.services.rest.api.usecases.spi.dao.UploadDao;
import de.consol.dus.s4.services.rest.api.usecases.spi.messaging.UploadReadyForProcessingEmitter;
import io.quarkus.runtime.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class UseCaseProducers {
  private final UploadDao uploadDao;
  private final UploadReadyForProcessingEmitter emitter;

  @Startup
  @Produces
  public AddPartToUploadUseCase addPartToUploadUseCase() {
    return AddPartToUploadUseCase
        .getInstance(uploadDao, getLoggerForClass(AddPartToUploadUseCase.class));
  }

  @Startup
  @Produces
  public CreateNewUploadUseCase createNewUploadUseCase() {
    return CreateNewUploadUseCase
        .getInstance(uploadDao, getLoggerForClass(CreateNewUploadUseCase.class));
  }

  @Startup
  @Produces
  public GetAllUploadsUseCase getAllUploadsUseCase() {
    return
        GetAllUploadsUseCase.getInstance(uploadDao, getLoggerForClass(GetAllUploadsUseCase.class));
  }

  @Startup
  @Produces
  public GetUploadByIdUseCase getUploadByIdUseCase() {
    return
        GetUploadByIdUseCase.getInstance(uploadDao, getLoggerForClass(GetUploadByIdUseCase.class));
  }

  @Startup
  @Produces
  public SetTotalPartsUseCase setNumPartsUseCase() {
    return
        SetTotalPartsUseCase.getInstance(uploadDao, getLoggerForClass(SetTotalPartsUseCase.class));
  }

  @Startup
  @Produces
  public DeleteUploadByIdUseCase deleteUploadUseCase() {
    return DeleteUploadByIdUseCase
            .getInstance(uploadDao, getLoggerForClass(DeleteUploadByIdUseCase.class));
  }


  @Startup
  @Produces
  public EnterProcessingStageUseCase enterProcessingStageUseCase() {
    return EnterProcessingStageUseCase
        .getInstance(uploadDao, getLoggerForClass(EnterProcessingStageUseCase.class), emitter);
  }

  private CorrelationLogger getLoggerForClass(Class<?> clazz) {
    return new CorrelationLogger(clazz);
  }
}
