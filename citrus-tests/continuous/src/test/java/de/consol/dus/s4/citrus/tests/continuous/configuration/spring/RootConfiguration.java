package de.consol.dus.s4.citrus.tests.continuous.configuration.spring;

import de.consol.dus.s4.citrus.tests.client.RestApiClient;
import de.consol.dus.s4.citrus.tests.config.Config;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({Config.class, RestApiClient.class})
public class RootConfiguration {
}
