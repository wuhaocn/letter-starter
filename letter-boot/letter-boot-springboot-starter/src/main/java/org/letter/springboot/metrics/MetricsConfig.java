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

	public static final String MONITOR_URL = "%s://%s:%s";
	public static final String TAG_HOST_IP = "hostip:%s";
	public static final String NODE_ID = "%s:%s";

	/**
	 * consul地址
	 */
	private String centerUrl = "http://127.0.0.1:18500/v1/agent/service/register";

	/**
	 * 服务名称
	 */
	private String name = "app";

	/**
	 * 节点名称
	 */
	private String id;

	/**
	 * 服务自动注销时间
	 */
	private String alive = "10m";
	/**
	 * 定期抓取时间
	 */
	private String interval = "30s";
	/**
	 * 抓取超时时间
	 */
	private String timeout = "5s";
	/**
	 * 主机IP
	 */
	private String ip;
	/**
	 * 监控端口
	 */
	private int port = 18080;

	/**
	 * 指标抓取url
	 */
	private String monitorUrl;

	/**
	 * 自定义指标
	 */
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

	public String getCenterUrl() {
		return centerUrl;
	}

	public void setCenterUrl(String centerUrl) {
		this.centerUrl = centerUrl;
	}

	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
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
		return String.format(MONITOR_URL, "http", getIp(), getPort());
	}

	public void setMonitorUrl(String monitorUrl) {
		this.monitorUrl = monitorUrl;
	}

	public List<String> getTag() {
		List<String> list = new ArrayList<>();
		list.addAll(tag);
		list.add(String.format(TAG_HOST_IP, getIp()));
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


}
