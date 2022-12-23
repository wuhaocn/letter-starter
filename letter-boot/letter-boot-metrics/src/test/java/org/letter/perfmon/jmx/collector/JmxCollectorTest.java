package org.letter.perfmon.jmx.collector;

import io.prometheus.client.CollectorRegistry;
import org.junit.jupiter.api.Test;

import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;
import java.util.logging.LogManager;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class JmxCollectorTest {


	public void OneTimeSetUp() throws Exception {

		LogManager.getLogManager().readConfiguration(JmxCollectorTest.class.getResourceAsStream("/logging.properties"));

		// Get the Platform MBean Server.
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

		// Register the MBeans.
		Cassandra.registerBean(mbs);
		CassandraMetrics.registerBean(mbs);
		Hadoop.registerBean(mbs);
		HadoopDataNode.registerBean(mbs);

		TomcatServlet.registerBean(mbs);
		Bool.registerBean(mbs);
		Camel.registerBean(mbs);

	}


	@Test
	public void testRulesMustHaveNameWithHelp() throws Exception {
		JmxCollector jc = new JmxCollector();
	}

	@Test()
	public void testRulesMustHaveNameWithLabels() throws Exception {
		JmxCollector jc = new JmxCollector();
	}

	@Test()
	public void testRulesMustHavePatternWithName() throws Exception {
		JmxCollector jc = new JmxCollector();
	}

	@Test
	public void testNameIsReplacedOnMatch() throws Exception {
		CollectorRegistry registry = new CollectorRegistry();
		JmxCollector jc = new JmxCollector().register(registry);
	}


}
