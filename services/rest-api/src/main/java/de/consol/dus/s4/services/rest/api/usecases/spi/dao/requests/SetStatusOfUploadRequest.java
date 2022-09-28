package de.consol.dus.s4.services.rest.api.usecases.spi.dao.requests;

import de.consol.dus.s4.services.rest.api.usecases.api.responses.UploadStatus;

public record SetStatusOfUploadRequest(long id, UploadStatus status) {
}
