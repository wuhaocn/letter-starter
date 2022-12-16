package org.letter.perfmon.test;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.dropwizard.DropwizardExports;
import io.prometheus.client.exporter.HTTPServer;
import org.letter.perform.jmx.BuildInfoCollector;
import org.letter.perform.jmx.JmxCollector;
import org.letter.perform.jmx.metrics.MetricRegistryManager;

import javax.management.MalformedObjectNameException;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author wuhao
 * @description: DropwizardExportsSelf
 * @createTime 2022/11/20 18:02:00
 */

public class JmxExporterTester{
	public static void main(String[] args) throws MalformedObjectNameException, IOException {
		System.setProperty("com.sun.management.jmxremote.rmi.port", "2199");
		System.setProperty("com.sun.management.jmxremote.port", "2199");
		System.setProperty("com.sun.management.jmxremote.ssl", "false");
		System.setProperty("com.sun.management.jmxremote", "true");

		InetSocketAddress socket = new InetSocketAddress("0.0.0.0", 8091);
		new BuildInfoCollector().register();
		new JmxCollector().register();
//		new HTTPServer(socket, CollectorRegistry.defaultRegistry);

//		MetricRegistryManager metricRegistryManager = MetricRegistryManager.getInstance();
//		CollectorRegistry.defaultRegistry.register(new DropwizardExports(metricRegistryManager.getMetricRegistry()));
//		HTTPServer httpServer = new HTTPServer("0.0.0.0", port);
	}
	
}