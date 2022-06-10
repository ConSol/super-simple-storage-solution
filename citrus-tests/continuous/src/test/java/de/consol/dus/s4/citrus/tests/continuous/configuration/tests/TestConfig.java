package de.consol.dus.s4.citrus.tests.continuous.configuration.tests;

import de.consol.dus.s4.citrus.tests.config.Config;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class TestConfig extends Config {
  private final String fileToUpload;
  private final int nPartsPerUpload;
  private final long sleepBetweenUploadsInMs;

  public TestConfig(
      @Value("${config.sut.url}") String sutUrl,
      @Value("${config.test.fileToUpload}") String fileToUpload,
      @Value("${config.test.nPartsPerUpload}") int nPartsPerUpload,
      @Value("${config.test.sleepBetweenUploadsInMs}") long sleepBetweenUploadsInMs) {
    super(sutUrl);
    this.fileToUpload = fileToUpload;
    this.nPartsPerUpload = nPartsPerUpload;
    this.sleepBetweenUploadsInMs = sleepBetweenUploadsInMs;
  }
}
