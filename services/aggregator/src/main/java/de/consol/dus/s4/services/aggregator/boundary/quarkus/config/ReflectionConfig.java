package de.consol.dus.s4.services.aggregator.boundary.quarkus.config;

import de.consol.dus.s4.services.aggregator.boundary.messaging.amqp.messages.UploadReadyForProcessing;
import de.consol.dus.s4.services.aggregator.boundary.messaging.amqp.requests.AggreagateUploadDataRequestImpl;
import de.consol.dus.s4.services.aggregator.usecases.api.exceptions.SingletonNotInitializedError;
import de.consol.dus.s4.services.aggregator.usecases.spi.dao.requests.WriteContentAndReleasePartsRequest;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection(targets = {
    UploadReadyForProcessing.class,
    SingletonNotInitializedError.class,
    AggreagateUploadDataRequestImpl.class,
    WriteContentAndReleasePartsRequest.class
})
public class ReflectionConfig {
}
