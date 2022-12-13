package org.letter.perfmon.test;

import com.codahale.metrics.Meter;
import io.prometheus.client.Counter;
import io.prometheus.client.Summary;
import org.letter.perform.exporter.MonitorExporter;
import org.letter.perform.metrics.PerfmonCounter;

import java.io.IOException;
import java.util.Random;

/**
 * @author wuhao
 * @createTime 2021-08-02 18:32:00
 */
public class MonitorExporterSimple {
	public static void main(String[] args) throws IOException {
		//启动监控server
		MonitorExporter.start(8901);

		System.out.println("MonitorServer.Start OK");

		//dropwizard
		PerfmonCounter.Timer timer = PerfmonCounter.timer("letter_rpc_dropwizard", "interface", "method");
		Meter meter = PerfmonCounter.meter("letter_rpc_dropwizard" , "interface", "method");
		//prometheus

//		Counter requests = Counter.build().name("letter_rpc_prometheus")
//				.labelNames("rpc", "service", "method")
//				.help("Total requests.").register();
//		Summary requestLatency = Summary.build()
//							.name("letter_rpc_prometheus_timer")
//							.help("request latency in seconds")
//							.quantile(0.5, 0.01)    // 0.5 quantile (median) with 0.01 allowed error
//							.quantile(0.95, 0.005)  // 0.95 quantile with 0.005 allowed error
//							.labelNames("rpc", "service", "method")
//							.register();
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
