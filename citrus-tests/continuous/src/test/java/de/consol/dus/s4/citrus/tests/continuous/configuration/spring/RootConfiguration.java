package de.consol.dus.s4.citrus.tests.continuous.configuration.spring;

import de.consol.dus.s4.citrus.tests.client.RestApiClient;
import de.consol.dus.s4.citrus.tests.continuous.configuration.tests.TestConfig;
import de.consol.dus.s4.citrus.tests.continuous.configuration.tests.Validators;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({TestConfig.class, RestApiClient.class, Validators.class})
public class RootConfiguration {
}
