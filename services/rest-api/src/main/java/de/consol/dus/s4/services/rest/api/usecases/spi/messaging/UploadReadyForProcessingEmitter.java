package de.consol.dus.s4.services.rest.api.usecases.spi.messaging;

import de.consol.dus.s4.services.rest.api.usecases.spi.messaging.requests.SendUploadReadyForProcessingRequest;

public interface UploadReadyForProcessingEmitter {
  void emit(SendUploadReadyForProcessingRequest request);
}
