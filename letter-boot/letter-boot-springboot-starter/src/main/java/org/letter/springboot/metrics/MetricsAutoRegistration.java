package org.letter.springboot.metrics;

import org.apache.commons.lang3.StringUtils;
import org.letter.perform.exporter.MonitorExporter;
import org.letter.perform.register.*;
import org.letter.springboot.CommonAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * MetricsAutoConfiguration
 *
 * @author letter
 * @createTime 2022/12/13 23:36:00
 */
public class MetricsAutoRegistration {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommonAutoConfiguration.class);

	public MetricsAutoRegistration() {
	}

	public void registerAndStarter(MetricsConfig config) {
		if (!check(config)) {
			return;
		}
		try {
			ServerRegistration registration = getServerRegistration(config);
			RegisterManager.register(config.getConsulUrl(), registration);
			MonitorExporter.start(config.getIp(), config.getPort(), config.getTag());
			LOGGER.info("MetricsAutoRegistration Complete:{}:{} To:{}", config.getIp(),
					config.getPort(), config.getConsulUrl());
		} catch (Exception e) {
			LOGGER.error("MetricsAutoRegistration registerAndStart Fail:{}:{}", config.getIp(), config.getPort(), e);
		}
	}

	public boolean check(MetricsConfig config) {
		if (StringUtils.isNotEmpty(config.getConsulUrl())) {
			return true;
		}
		return true;
	}

	public ServerRegistration getServerRegistration(MetricsConfig config) {
		ServerRegistration registration = new ServerRegistration();
		List<String> tags = new ArrayList<>();
		tags.add("service");

		List<ServerCheck> checks = new ArrayList<>();
		ServerCheck serverCheck = new ServerCheck();
		serverCheck.setHttp(config.getMonitorUrl());
		checks.add(serverCheck);

		ServerMeta serverMeta = new ServerMeta();
		serverMeta.setApp(config.getName());
		ServerWeights serverWeights = new ServerWeights();

		registration.setId(config.getId())
				.setName(config.getName())
				.setAddress(config.getIp())
				.setPort(config.getPort())
				.setTags(tags)
				.setMeta(serverMeta)
				.setWeights(serverWeights)
				.setChecks(checks);
		return registration;
	}
}
