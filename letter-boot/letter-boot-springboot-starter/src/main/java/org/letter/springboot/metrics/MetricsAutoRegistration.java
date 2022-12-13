package org.letter.springboot.metrics;

import io.micrometer.core.instrument.util.StringUtils;
import org.letter.perform.exporter.MonitorExporter;
import org.letter.perform.register.RegisterManager;
import org.letter.perform.register.ServerCheck;
import org.letter.perform.register.ServerMeta;
import org.letter.perform.register.ServerRegistration;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wuhao
 * @description: MetricsAutoConfiguration
 * @createTime 2022/12/13 23:36:00
 */
public class MetricsAutoRegistration {

	public MetricsAutoRegistration() {
	}
	public void registerAndStart(MetricsConfig config){
		if (!check(config)){
			return;
		}
		try {
			ServerRegistration registration = getServerRegistration(config);
			RegisterManager.register(config.getConsulUrl(), registration);
			MonitorExporter.start(config.getPort());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean check(MetricsConfig config){
		if (StringUtils.isNotEmpty(config.getConsulUrl())){
			return true;
		}
		return false;
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
		return null;
	}
}
