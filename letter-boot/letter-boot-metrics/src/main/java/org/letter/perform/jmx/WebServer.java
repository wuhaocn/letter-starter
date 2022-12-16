package org.letter.perform.jmx;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.HTTPServer;

import java.io.File;
import java.net.InetSocketAddress;

public class WebServer {

   public static void main(String[] args) throws Exception {
   	 String ip = "127.0.0.1";
   	 int port = 8091;
     InetSocketAddress socket = new InetSocketAddress(ip, port);
     new BuildInfoCollector().register();
     new JmxCollector().register();
     new HTTPServer(socket, CollectorRegistry.defaultRegistry);
     System.out.println("WebServer Start OK " + ip + ":" + port);
   }
}
