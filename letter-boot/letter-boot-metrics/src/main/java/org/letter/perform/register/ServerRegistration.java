package org.letter.perform.register;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wuhao
 * @description: RegisterBean
 * @createTime 2022/11/15 16:56:00
 */

public class ServerRegistration {
	private String id;
	private String name;
	private List<String> tags;
	private String address;
	private int port;
	private ServerMeta meta;
	private boolean enableTagOverride;
	private List<ServerCheck> checks;
	private ServerWeights weights;

	public String getId() {
		return id;
	}

	public ServerRegistration setId(String id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public ServerRegistration setName(String name) {
		this.name = name;
		return this;
	}

	public List<String> getTags() {
		return tags;
	}

	public ServerRegistration setTags(List<String> tags) {
		this.tags = tags;
		return this;
	}

	public String getAddress() {
		return address;
	}

	public ServerRegistration setAddress(String address) {
		this.address = address;
		return this;
	}

	public int getPort() {
		return port;
	}

	public ServerRegistration setPort(int port) {
		this.port = port;
		return this;
	}

	public ServerMeta getMeta() {
		return meta;
	}

	public ServerRegistration setMeta(ServerMeta meta) {
		this.meta = meta;
		return this;
	}

	public boolean isEnableTagOverride() {
		return enableTagOverride;
	}

	public ServerRegistration setEnableTagOverride(boolean enableTagOverride) {
		this.enableTagOverride = enableTagOverride;
		return this;
	}

	public List<ServerCheck> getChecks() {
		return checks;
	}

	public ServerRegistration setChecks(List<ServerCheck> checks) {
		this.checks = checks;
		return this;
	}

	public ServerWeights getWeights() {
		return weights;
	}

	public ServerRegistration setWeights(ServerWeights weights) {
		this.weights = weights;
		return this;
	}
}
/**
 {
 "id": "test1",
 "name": "test1",
 "address": "192.168.1.6",
 "port": 9100,
 "tags": ["service"],
 "checks": [{
 "http": "http://192.168.1.6:9100/",
 "interval": "10s"
 }]
 }
 */
