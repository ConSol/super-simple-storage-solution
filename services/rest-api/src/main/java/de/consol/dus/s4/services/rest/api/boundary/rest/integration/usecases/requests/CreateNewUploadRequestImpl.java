package de.consol.dus.s4.services.rest.api.boundary.rest.integration.usecases.requests;

import de.consol.dus.s4.services.rest.api.usecases.api.requests.CreateNewUploadRequest;

public record CreateNewUploadRequestImpl(String fileName) implements CreateNewUploadRequest {
}
