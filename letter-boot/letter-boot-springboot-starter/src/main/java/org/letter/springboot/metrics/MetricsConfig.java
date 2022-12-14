package org.letter.springboot.metrics;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.InetAddress;

/**
 * MetricsConfig
 *
 * @author letter
 **/
@ConfigurationProperties(prefix = "monitor")
public class MetricsConfig {
	private String appName = "app";
	private String consulUrl = "http://127.0.0.1:18500/v1/agent/service/register";
	private int aliveTime = 5;
	private int port = 18080;
	private String ip = null;
	private String checkUrl = null;

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
		if (StringUtils.isNotEmpty(ip)){
			return ip;
		}
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e){

		}
		return "127.0.0.1";
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCheckUrl() {
		if (StringUtils.isNotEmpty(checkUrl)){
			return checkUrl;
		}
		return ip + ":" + port;
	}

	public void setCheckUrl(String checkUrl) {
		this.checkUrl = checkUrl;
	}

}
