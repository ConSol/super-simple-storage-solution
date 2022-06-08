package de.consol.dus.s4.commons.micrometer.extras;

import io.github.mweirauch.micrometer.jvm.extras.ProcessMemoryMetrics;
import io.github.mweirauch.micrometer.jvm.extras.ProcessThreadMetrics;
import io.micrometer.core.instrument.binder.MeterBinder;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@Singleton
public class ExtrasMeterBinder {
  @Produces
  public MeterBinder processMemoryMetrics() {
    return new ProcessMemoryMetrics();
  }

  @Produces
  public MeterBinder processThreadMetrics() {
    return new ProcessThreadMetrics();
  }
}
