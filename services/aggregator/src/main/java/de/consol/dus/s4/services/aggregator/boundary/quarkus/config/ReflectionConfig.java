package de.consol.dus.s4.services.aggregator.boundary.quarkus.config;

import de.consol.dus.s4.services.aggregator.boundary.messaging.amqp.messages.UploadReadyForProcessing;
import de.consol.dus.s4.services.aggregator.usecases.spi.dao.requests.WriteContentAndReleasePartsRequest;
import io.quarkus.runtime.annotations.RegisterForReflection;

@SuppressWarnings("unused")
@RegisterForReflection(targets = {
    UploadReadyForProcessing.class,
    WriteContentAndReleasePartsRequest.class
})
public class ReflectionConfig {
}
