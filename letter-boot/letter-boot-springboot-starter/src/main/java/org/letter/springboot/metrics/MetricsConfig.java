package org.letter.springboot.metrics;

import org.apache.commons.lang3.StringUtils;
import org.letter.common.utils.NetUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * MetricsConfig
 *
 * @author letter
 **/
@ConfigurationProperties(prefix = "monitor")
public class MetricsConfig {
	private String id;
	private String name = "app";
	private String consulUrl = "http://127.0.0.1:18500/v1/agent/service/register";
	private String alive = "1m";
	private String interval = "20s";
	private int port = 18080;
	private String ip;
	private String monitorUrl;
	private List<String> tag = new ArrayList<>();

	public String getId() {
		if (StringUtils.isNotEmpty(id)) {
			return id;
		}
		id = UUID.randomUUID().toString();
		return getName() + "_" + id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getConsulUrl() {
		return consulUrl;
	}

	public void setConsulUrl(String consulUrl) {
		this.consulUrl = consulUrl;
	}

	public String getAlive() {
		return alive;
	}

	public void setAlive(String alive) {
		this.alive = alive;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMonitorUrl() {
		if (StringUtils.isNotEmpty(monitorUrl)) {
			return monitorUrl;
		}
		return String.format(URL_STR, "http", getIp(), getPort());
	}

	public void setMonitorUrl(String monitorUrl) {
		this.monitorUrl = monitorUrl;
	}

	public List<String> getTag() {
		List<String> list = new ArrayList<>();
		list.addAll(tag);
		list.add(String.format(HOST_IP, "hostip", getIp()));
		return list;
	}

	public void setTag(List<String> tag) {
		this.tag = tag;
	}

	public String getIp() {
		try {
			if (StringUtils.isNotEmpty(ip)) {
				return ip;
			}
			return NetUtils.getLocalAddress0().getHostAddress();
		} catch (Exception e) {
			return "127.0.0.1";
		}
	}

	public static final String URL_STR = "%s://%s:%s";
	public static final String HOST_IP = "%s:%s";
}
