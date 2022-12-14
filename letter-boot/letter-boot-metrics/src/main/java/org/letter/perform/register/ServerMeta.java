package org.letter.perform.register;

/**
 * @author wuhao
 * @description: ServerMeta
 * @createTime 2022/11/15 16:58:00
 */

public class ServerMeta {
	private String version = "1.0";
	private String item = "app";
	private String app = "app";

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}
}
