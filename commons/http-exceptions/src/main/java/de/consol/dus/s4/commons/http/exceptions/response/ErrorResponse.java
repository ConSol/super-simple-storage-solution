package de.consol.dus.s4.commons.http.exceptions.response;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record ErrorResponse(String message) {
}
