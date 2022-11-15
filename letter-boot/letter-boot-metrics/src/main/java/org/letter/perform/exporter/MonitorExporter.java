package org.letter.perform.exporter;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.dropwizard.DropwizardExports;
import io.prometheus.client.exporter.HTTPServer;
import org.letter.perform.metrics.MetricRegistryManager;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author wuhao
 * @createTime 2021-07-23 13:53:00
 */
public class MonitorExporter {
	public static AtomicBoolean atomicBoolean = new AtomicBoolean(false);

	public static void start(int port) throws IOException {
		if (atomicBoolean.compareAndSet(false, true)) {
			MetricRegistryManager metricRegistryManager = MetricRegistryManager.getInstance();
			CollectorRegistry.defaultRegistry.register(new DropwizardExports(metricRegistryManager.getMetricRegistry()));
			HTTPServer httpServer = new HTTPServer(port);

		}
	}
}
