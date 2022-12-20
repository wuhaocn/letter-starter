package org.letter.perform.exporter;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.dropwizard.DropwizardExports;
import io.prometheus.client.exporter.HTTPServer;
import org.letter.perform.jmx.collector.JmxCollector;
import org.letter.perform.jmx.metrics.MetricRegistryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author letter
 * @createTime 2021-07-23 13:53:00
 */
public class MonitorExporter {
	private static AtomicBoolean atomicBoolean = new AtomicBoolean(false);
	private static final Logger LOGGER = LoggerFactory.getLogger(MetricRegistryManager.class);

	public static void start(String ip, int port){
		try {
			if (atomicBoolean.compareAndSet(false, true)) {
				MetricRegistryManager.getInstance();
				JmxCollector jmxCollector = new JmxCollector();
				jmxCollector.setTag(Arrays.asList("key1", "key2"),
						Arrays.asList("value1", "value2"));
				CollectorRegistry.defaultRegistry.register(jmxCollector);
				HTTPServer httpServer = new HTTPServer(ip, port);
				LOGGER.info("MonitorExporter.Start OK. {}://{}:{}", "http", ip, port);
			}
		} catch (Exception e) {
			LOGGER.error("MonitorExporter.Start Exception:{}:{}", ip, port, e);
		}


	}
}
