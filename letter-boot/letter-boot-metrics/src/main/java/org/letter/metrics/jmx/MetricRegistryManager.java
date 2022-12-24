package org.letter.metrics.jmx;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jmx.JmxReporter;
import com.codahale.metrics.jvm.*;
import org.letter.metrics.utils.MetricsConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

import static org.letter.metrics.utils.MetricsConstant.*;

/**
 * MetricRegistryManager
 *
 * @author letter
 */
public class MetricRegistryManager {

	private static final MetricRegistry METRIC = new MetricRegistry();

	protected static final Logger LOGGER = LoggerFactory.getLogger(MetricRegistryManager.class);

	static {
		METRIC.register("jvm_fd_usage", new FileDescriptorRatioGauge());
		METRIC.register("jvm_gc", new GarbageCollectorMetricSet());
		METRIC.register("jvm_memory", new MemoryUsageGaugeSet());
		METRIC.register("jvm_thread_states", new ThreadStatesGaugeSet());

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
			ObjectName metricsConfig = new ObjectName(MetricsConstant.METRICS_CONFIG);
			server.registerMBean(this.conf, metricsConfig);
			ObjectName metricsInfo = new ObjectName(METRICS_SERVICE);
			server.registerMBean(new MetricsServiceInfo(), metricsInfo);
			LOGGER.info("registerMbean Complete: {}  {}", METRICS_CONFIG, METRICS_SERVICE);
		} catch (Exception e) {
			LOGGER.warn("registerMbean Error: {}  {}", METRICS_CONFIG, METRICS_SERVICE, e);
		}
	}

	public boolean needMetrics() {
		return this.conf.needMetrics();
	}

	public MetricRegistry getMetricRegistry() {
		return METRIC;
	}
}
