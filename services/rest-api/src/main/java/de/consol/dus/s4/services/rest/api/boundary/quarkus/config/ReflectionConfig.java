package de.consol.dus.s4.services.rest.api.boundary.quarkus.config;

import de.consol.dus.s4.services.rest.api.boundary.dao.integration.usecases.responses.UploadImpl;
import de.consol.dus.s4.services.rest.api.boundary.dao.integration.usecases.responses.UploadPartImpl;
import de.consol.dus.s4.services.rest.api.boundary.messaging.amqp.messages.UploadReadyForProcessing;
import de.consol.dus.s4.services.rest.api.boundary.rest.integration.usecases.requests.AddPartToUploadRequestImpl;
import de.consol.dus.s4.services.rest.api.boundary.rest.integration.usecases.requests.CreateNewUploadRequestImpl;
import de.consol.dus.s4.services.rest.api.boundary.rest.request.AddPartToUploadRequest;
import de.consol.dus.s4.services.rest.api.boundary.rest.request.SetTotalPartsOfUploadRequest;
import de.consol.dus.s4.services.rest.api.boundary.rest.request.StartUploadRequest;
import de.consol.dus.s4.services.rest.api.boundary.rest.response.UploadResponse;
import de.consol.dus.s4.services.rest.api.usecases.api.exceptions.PartNumberAlreadyExistsException;
import de.consol.dus.s4.services.rest.api.usecases.api.exceptions.PartNumberIsBiggerThanTotalParts;
import de.consol.dus.s4.services.rest.api.usecases.api.exceptions.TotalPartsAlreadySetException;
import de.consol.dus.s4.services.rest.api.usecases.api.exceptions.TotalPartsSmallerThanMaxPartNumberException;
import de.consol.dus.s4.services.rest.api.usecases.spi.dao.requests.SetStatusOfUploadRequest;
import de.consol.dus.s4.services.rest.api.usecases.spi.messaging.requests.SendUploadReadyForProcessingRequest;
import io.quarkus.runtime.annotations.RegisterForReflection;

@SuppressWarnings("unused")
@RegisterForReflection(targets = {
    UploadImpl.class,
    UploadPartImpl.class,
    UploadReadyForProcessing.class,
    AddPartToUploadRequestImpl.class,
    CreateNewUploadRequestImpl.class,
    AddPartToUploadRequest.class,
    SetTotalPartsOfUploadRequest.class,
    StartUploadRequest.class,
    UploadResponse.class,
    PartNumberAlreadyExistsException.class,
    PartNumberIsBiggerThanTotalParts.class,
    TotalPartsAlreadySetException.class,
    TotalPartsSmallerThanMaxPartNumberException.class,
    SetStatusOfUploadRequest.class,
    SendUploadReadyForProcessingRequest.class
})
public class ReflectionConfig {
}
