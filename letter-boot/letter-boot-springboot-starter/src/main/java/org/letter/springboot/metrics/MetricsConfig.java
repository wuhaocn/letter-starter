package org.letter.springboot.metrics;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * MetricsConfig
 *
 * @author letter
 **/
@ConfigurationProperties(prefix = "monitor")
public class MetricsConfig {
	private String appName = "app";
	private String consulUrl;
	private int aliveTime;
	private int port;
	private String ip;
	private String checkUrl;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getConsulUrl() {
		return consulUrl;
	}

	public void setConsulUrl(String consulUrl) {
		this.consulUrl = consulUrl;
	}

	public int getAliveTime() {
		return aliveTime;
	}

	public void setAliveTime(int aliveTime) {
		this.aliveTime = aliveTime;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}


	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCheckUrl() {
		return checkUrl;
	}

	public void setCheckUrl(String checkUrl) {
		this.checkUrl = checkUrl;
	}

}
