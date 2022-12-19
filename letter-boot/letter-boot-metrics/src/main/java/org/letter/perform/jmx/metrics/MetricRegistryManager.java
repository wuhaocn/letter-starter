package org.letter.perform.jmx.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jmx.JmxReporter;
import com.codahale.metrics.jvm.FileDescriptorRatioGauge;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * MetricRegistryManager
 *
 * @author letter
 */
public class MetricRegistryManager {

	private static final MetricRegistry METRIC = new MetricRegistry();
	private static final String METRICS_CONFIG = "letter_metrics:name=MetricsConfig";
	private static final String METRICS_SERVICE = "letter_metrics:name=MetricsServiceInfo";
	protected static final Logger LOGGER = LoggerFactory.getLogger(MetricRegistryManager.class);

	static {
		METRIC.register("jvm.gc", new GarbageCollectorMetricSet());
		METRIC.register("jvm.memory", new MemoryUsageGaugeSet());
		METRIC.register("jvm.thread-states", new ThreadStatesGaugeSet());
		METRIC.register("jvm.fd.usage", new FileDescriptorRatioGauge());
	}

	private static volatile MetricRegistryManager instance = null;

	public static MetricRegistryManager getInstance() {
		if (instance == null) {
			synchronized (MetricRegistryManager.class) {
				if (instance == null) {
					instance = new MetricRegistryManager();
				}
			}
		}
		return instance;
	}
	
	private final JmxReporter reporter;
	private final MetricsConfig conf;
	
	private MetricRegistryManager() {
		this.conf = new MetricsConfig();
		this.reporter = JmxReporter.forRegistry(METRIC).build();
		this.reporter.start();
		this.registerMbean();
	}

	private void registerMbean() {
		try {
			MBeanServer server = ManagementFactory.getPlatformMBeanServer();
			ObjectName metricsConfig = new ObjectName(METRICS_CONFIG);
			server.registerMBean(this.conf, metricsConfig);
			ObjectName metricsInfo = new ObjectName(METRICS_SERVICE);
			server.registerMBean(new MetricsServiceInfo(), metricsInfo);
			LOGGER.info("registerMbean Complete: {}  {}", METRICS_CONFIG, METRICS_SERVICE);
		} catch (Exception e) {
			LOGGER.warn("registerMbean Error: {}  {}", METRICS_CONFIG, METRICS_SERVICE);
		}
	}

	public boolean needMetrics() {
		return this.conf.needMetrics();
	}

	public MetricRegistry getMetricRegistry() {
		return METRIC;
	}
}
