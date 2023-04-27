package de.consol.dus.s4.commons.opentracing.messaging.amqp;

import io.smallrye.reactive.messaging.MessageConverter;
import io.smallrye.reactive.messaging.amqp.IncomingAmqpMetadata;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.MediaType;
import java.lang.reflect.Type;
import org.eclipse.microprofile.reactive.messaging.Message;

@ApplicationScoped
public class JsonToObjectConverter implements MessageConverter {

  @Override
  public boolean canConvert(Message<?> in, Type target) {
    return target instanceof Class
        && in.getMetadata(IncomingAmqpMetadata.class)
        .map(IncomingAmqpMetadata::getContentType)
        .map(MediaType.APPLICATION_JSON::equals)
        .orElse(false);
  }

  @Override
  public Message<?> convert(Message<?> in, Type target) {
    return in.withPayload(((JsonObject) in.getPayload()).mapTo((Class<?>) target));
  }
}