package org.letter.perfmon.test;

import com.codahale.metrics.Meter;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.dropwizard.DropwizardExports;
import io.prometheus.client.exporter.HTTPServer;
import org.letter.perform.exporter.MonitorExporter;
import org.letter.perform.jmx.BuildInfoCollector;
import org.letter.perform.jmx.JmxCollector;
import org.letter.perform.jmx.JmxMBeanPropertyCache;
import org.letter.perform.jmx.JmxScraper;
import org.letter.perform.jmx.metrics.MetricRegistryManager;
import org.letter.perform.metrics.PerfmonCounter;

import javax.management.MalformedObjectNameException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

/**
 * @author wuhao
 * @createTime 2021-08-02 18:32:00
 */
public class MonitorExporterSimpleOther {
	public static void main(String[] args) throws Exception {
		MetricRegistryManager.getInstance();
		new BuildInfoCollector().register();
		System.setProperty("com.sun.management.jmxremote.rmi.port", "2199");
		System.setProperty("com.sun.management.jmxremote.port", "2199");
		System.setProperty("com.sun.management.jmxremote.ssl", "false");
		System.setProperty("com.sun.management.jmxremote.ssl", "false");
		System.setProperty("com.sun.management.jmxremote", "true");
		
		CollectorRegistry.defaultRegistry.register(new JmxCollector());
		//启动监控server
//		MetricRegistryManager metricRegistryManager = MetricRegistryManager.getInstance();
//		CollectorRegistry.defaultRegistry.register(new DropwizardExports(metricRegistryManager.getMetricRegistry()));
		HTTPServer httpServer = new HTTPServer("0.0.0.0", 8901);

		System.out.println("MonitorServer.Start OK");

		//dropwizard
		PerfmonCounter.Timer timer = PerfmonCounter.timer("letter_rpc_dropwizard", "interface", "method");
		Meter meter = PerfmonCounter.meter("letter_rpc_dropwizard" , "interface", "method");
		//prometheus
		
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true){
					PerfmonCounter.Timer.Context context = timer.time();


					try {
						int i = new Random().nextInt(20);
						meter.mark();
//						requests.labels("rpc" + i, "service" + i, "method" + i).inc();
						//Summary.Timer timer1 = requestLatency.labels("rpc" + i, "service" + i, "method" + i).startTimer();
						Thread.sleep(i);
						context.stop();
						//timer1.close();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		});
		thread.start();

	}
}
