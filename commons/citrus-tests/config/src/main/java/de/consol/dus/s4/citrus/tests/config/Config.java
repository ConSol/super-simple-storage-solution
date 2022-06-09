package de.consol.dus.s4.citrus.tests.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ComponentScan("de.consol.dus.s4.citrus.tests")
public class Config {
  private final String sutUrl;

  public Config(@Value("${config.sut.url}") String sutUrl) {
    this.sutUrl = sutUrl;
  }
}
