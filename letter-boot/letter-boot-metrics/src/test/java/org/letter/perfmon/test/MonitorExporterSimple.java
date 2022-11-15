package org.letter.perfmon.test;

import org.letter.perform.exporter.MonitorExporter;

import java.io.IOException;

/**
 * @author wuhao
 * @createTime 2021-08-02 18:32:00
 */
public class MonitorExporterSimple {
	public static void main(String[] args) throws IOException {
		//启动监控server
		MonitorExporter.start(8901);


		System.out.println("MonitorServer.complete");


	}
}
