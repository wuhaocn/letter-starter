package org.letter.springboot.metrics;

import org.apache.commons.lang3.StringUtils;
import org.letter.perform.exporter.MonitorExporter;
import org.letter.perform.register.RegisterManager;
import org.letter.perform.register.ServerCheck;
import org.letter.perform.register.ServerMeta;
import org.letter.perform.register.ServerRegistration;
import org.letter.springboot.CommonAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
	public void registerAndStart(MetricsConfig config){
		if (!check(config)){
			return;
		}
		try {
			ServerRegistration registration = getServerRegistration(config);
			RegisterManager.register(config.getConsulUrl(), registration);
			MonitorExporter.start(config.getIp(), config.getPort());
			LOGGER.info("MetricsAutoRegistration Complete:{}:{} To:{}", config.getIp(),
					config.getPort(), config.getConsulUrl());
		} catch (Exception e) {
			LOGGER.error("MetricsAutoRegistration registerAndStart Fail:{}:{}", config.getIp(), config.getPort(), e);
		}
	}
	public boolean check(MetricsConfig config){
		if (StringUtils.isNotEmpty(config.getConsulUrl())){
			return true;
		}
		return true;
	}
	public ServerRegistration getServerRegistration(MetricsConfig config) {
		ServerRegistration registration = new ServerRegistration();
		List<String> tags = new ArrayList<>();
		List<ServerCheck> checks = new ArrayList<>();
		ServerCheck serverCheck = new ServerCheck();
		serverCheck.setHttp(config.getCheckUrl());
		tags.add("service");
		registration.setId(config.getAppName())
				.setName(config.getAppName())
				.setAddress(config.getIp())
				.setPort(config.getPort())
				.setTags(tags)
				.setMeta(new ServerMeta())
				.setChecks(checks);
		return registration;
	}
}
