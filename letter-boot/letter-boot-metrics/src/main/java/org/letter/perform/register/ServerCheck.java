package org.letter.perform.register;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wuhao
 * @description: ServerCheck
 * @createTime 2022/11/15 16:59:00
 */

public class ServerCheck {
	/**
	 * 90m
	 */
	private String deregisterCriticalServiceAfter = "90m";
	/**
	 * ["/usr/local/bin/check_redis.py"]
	 */
	private String http;

	/**
	 * "10s"
	 */
	private String interval = "10s";

	/**
	 * "5s"
	 */
	private String timeout = "5s";

	public String getDeregisterCriticalServiceAfter() {
		return deregisterCriticalServiceAfter;
	}

	public void setDeregisterCriticalServiceAfter(String deregisterCriticalServiceAfter) {
		this.deregisterCriticalServiceAfter = deregisterCriticalServiceAfter;
	}

	public String getHttp() {
		return http;
	}

	public void setHttp(String http) {
		this.http = http;
	}

	public String getInterval() {
		return interval;
	}

	public ServerCheck setInterval(String interval) {
		this.interval = interval;
		return this;
	}

	public String getTimeout() {
		return timeout;
	}

	public ServerCheck setTimeout(String timeout) {
		this.timeout = timeout;
		return this;
	}
}
