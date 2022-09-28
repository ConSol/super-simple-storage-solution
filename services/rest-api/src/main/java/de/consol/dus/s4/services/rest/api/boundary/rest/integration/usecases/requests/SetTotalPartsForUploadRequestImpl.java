package de.consol.dus.s4.services.rest.api.boundary.rest.integration.usecases.requests;

import de.consol.dus.s4.services.rest.api.usecases.api.requests.SetTotalPartsForUploadRequest;

public record SetTotalPartsForUploadRequestImpl(long id, int totalParts)
    implements SetTotalPartsForUploadRequest {
}
