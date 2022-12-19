package org.letter.perform.jmx.collector;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.HTTPServer;
import org.letter.perform.jmx.metrics.MetricRegistryManager;

import java.net.InetSocketAddress;
import java.util.Arrays;

public class WebServer {


	public static void start(String ip, int port) throws Exception {

		MetricRegistryManager.getInstance();
		new BuildInfoCollector().register();
		JmxCollector jmxCollector = new JmxCollector();
		jmxCollector.setTag(Arrays.asList("cluster"),
				Arrays.asList("prod"));
		CollectorRegistry.defaultRegistry.register(jmxCollector);
		HTTPServer httpServer = new HTTPServer(ip, port);
		System.out.println("MonitorServer.Start OK");
	}

	public static void main(String[] args) throws Exception {
		String ip = "127.0.0.1";
		int port = 8091;
		start(ip, port);
	}
}
