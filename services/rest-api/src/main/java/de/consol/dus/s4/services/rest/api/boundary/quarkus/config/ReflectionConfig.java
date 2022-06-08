package de.consol.dus.s4.services.rest.api.boundary.quarkus.config;

import de.consol.dus.s4.services.rest.api.boundary.dao.integration.usecases.responses.UploadImpl;
import de.consol.dus.s4.services.rest.api.boundary.dao.integration.usecases.responses.UploadPartImpl;
import de.consol.dus.s4.services.rest.api.boundary.messaging.amqp.messages.UploadReadyForProcessing;
import de.consol.dus.s4.services.rest.api.boundary.rest.integration.usecases.requests.AddPartToUploadRequestImpl;
import de.consol.dus.s4.services.rest.api.boundary.rest.integration.usecases.requests.CreateNewUploadRequestImpl;
import de.consol.dus.s4.services.rest.api.boundary.rest.integration.usecases.requests.DeleteUploadByIdRequestImpl;
import de.consol.dus.s4.services.rest.api.boundary.rest.integration.usecases.requests.GetAllUploadsRequestImpl;
import de.consol.dus.s4.services.rest.api.boundary.rest.integration.usecases.requests.GetUploadByIdRequestImpl;
import de.consol.dus.s4.services.rest.api.boundary.rest.request.AddPartToUploadRequest;
import de.consol.dus.s4.services.rest.api.boundary.rest.request.SetTotalPartsOfUploadRequest;
import de.consol.dus.s4.services.rest.api.boundary.rest.request.StartUploadRequest;
import de.consol.dus.s4.services.rest.api.boundary.rest.response.UploadResponse;
import de.consol.dus.s4.services.rest.api.usecases.api.exceptions.PartNumberAlreadyExistsException;
import de.consol.dus.s4.services.rest.api.usecases.api.exceptions.PartNumberIsBiggerThanTotalParts;
import de.consol.dus.s4.services.rest.api.usecases.api.exceptions.SingletonNotInitializedError;
import de.consol.dus.s4.services.rest.api.usecases.api.exceptions.TotalPartsAlreadySetException;
import de.consol.dus.s4.services.rest.api.usecases.api.exceptions.TotalPartsSmallerThanMaxPartNumberException;
import de.consol.dus.s4.services.rest.api.usecases.internal.api.EnterProcessingRequest;
import de.consol.dus.s4.services.rest.api.usecases.spi.dao.requests.SetStatusOfUploadRequest;
import de.consol.dus.s4.services.rest.api.usecases.spi.messaging.requests.SendUploadReadyForProcessingRequest;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.jboss.resteasy.reactive.multipart.FileUpload;

@RegisterForReflection(targets = {
    UploadImpl.class,
    UploadPartImpl.class,
    UploadReadyForProcessing.class,
    AddPartToUploadRequestImpl.class,
    CreateNewUploadRequestImpl.class,
    DeleteUploadByIdRequestImpl.class,
    GetAllUploadsRequestImpl.class,
    GetUploadByIdRequestImpl.class,
    AddPartToUploadRequest.class,
    SetTotalPartsOfUploadRequest.class,
    StartUploadRequest.class,
    UploadResponse.class,
    PartNumberAlreadyExistsException.class,
    PartNumberIsBiggerThanTotalParts.class,
    SingletonNotInitializedError.class,
    TotalPartsAlreadySetException.class,
    TotalPartsSmallerThanMaxPartNumberException.class,
    EnterProcessingRequest.class,
    SetStatusOfUploadRequest.class,
    SendUploadReadyForProcessingRequest.class
})
public class ReflectionConfig {
}
