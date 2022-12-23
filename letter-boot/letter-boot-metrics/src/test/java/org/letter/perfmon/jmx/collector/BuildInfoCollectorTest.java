package org.letter.perfmon.jmx.collector;

import io.prometheus.client.CollectorRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class BuildInfoCollectorTest {

  private CollectorRegistry registry = new CollectorRegistry();

  @BeforeEach
  public void setUp() {
    new BuildInfoCollector().register(registry);
  }

  @Test
  public void testBuildInfo() {
    String version = this.getClass().getPackage().getImplementationVersion();
    String name = this.getClass().getPackage().getImplementationTitle();

    assertEquals(
            1L,
            registry.getSampleValue(
                    "jmx_exporter_build_info", new String[]{"version", "name"}, new String[]{
                            version != null ? version : "unknown",
                            name != null ? name : "unknown"
                    }),
            .0000001);
  }
}
