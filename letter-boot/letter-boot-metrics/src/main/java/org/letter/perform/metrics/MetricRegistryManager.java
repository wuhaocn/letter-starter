package org.letter.perform.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jvm.FileDescriptorRatioGauge;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;

/**
 * MetricRegistryManager
 *
 * @author wuhao
 */
public class MetricRegistryManager {

	private static final MetricRegistry METRIC = new MetricRegistry();

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

	private MetricRegistryManager() {
	}

	public MetricRegistry getMetricRegistry() {
		return METRIC;
	}
}
