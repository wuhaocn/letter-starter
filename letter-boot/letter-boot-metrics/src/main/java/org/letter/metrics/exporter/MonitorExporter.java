package org.letter.metrics.exporter;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.HTTPServer;
import org.letter.metrics.collector.jmx.JmxCollector;
import org.letter.metrics.jmx.MetricRegistryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author letter
 * @createTime 2021-07-23 13:53:00
 */
public class MonitorExporter {
	private static AtomicBoolean atomicBoolean = new AtomicBoolean(false);
	private static final Logger LOGGER = LoggerFactory.getLogger(MetricRegistryManager.class);

	/**
	 * 启动jmx exporter
	 *
	 * @param ip 启动IP
	 * @param port 端口
	 * @param tags 标签
	 */
	public static void start(String ip, int port, List<String> tags) {
		try {
			if (atomicBoolean.compareAndSet(false, true)) {
				MetricRegistryManager.getInstance();
				JmxCollector jmxCollector = new JmxCollector();
				updateTag(jmxCollector, tags);
				CollectorRegistry.defaultRegistry.register(jmxCollector);
				HTTPServer httpServer = new HTTPServer(ip, port);
				LOGGER.info("MonitorExporter.Start OK. {}://{}:{}", "http", ip, port);
			}
		} catch (Exception e) {
			LOGGER.error("MonitorExporter.Start Exception:{}:{}", ip, port, e);
		}


	}

	public static void updateTag(JmxCollector jmxCollector, List<String> tag) {
		List<String> names = new ArrayList<>();
		List<String> values = new ArrayList<>();
		for (String item : tag){
			String[] app = item.split(":");
			names.add(app[0]);
			values.add(app[1]);
		}
		jmxCollector.setTag(names, values);
	}
}
