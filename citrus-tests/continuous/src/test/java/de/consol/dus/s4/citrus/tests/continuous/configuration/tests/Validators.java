package de.consol.dus.s4.citrus.tests.continuous.configuration.tests;

import com.consol.citrus.validation.json.JsonPathMessageValidator;
import com.consol.citrus.validation.text.PlainTextMessageValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Validators {
  @Bean
  PlainTextMessageValidator defaultPlainTextMessageValidator() {
    return new PlainTextMessageValidator();
  }

  @Bean
  JsonPathMessageValidator defaultJsonPathMessageValidator() {
    return new JsonPathMessageValidator();
  }
}
