package org.letter.perform.jmx;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.HTTPServer;

import java.io.File;
import java.net.InetSocketAddress;

public class WebServer {

   public static void main(String[] args) throws Exception {


     InetSocketAddress socket = new InetSocketAddress("0.0.0.0", 8091);
     new BuildInfoCollector().register();
     new JmxCollector(new File(args[1]), JmxCollector.Mode.STANDALONE).register();
     new HTTPServer(socket, CollectorRegistry.defaultRegistry);
   }
}
