package org.letter.perfmon.test;

import org.letter.perform.register.RegisterManager;
import org.letter.perform.register.ServerCheck;
import org.letter.perform.register.ServerMeta;
import org.letter.perform.register.ServerRegistration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wuhao
 * @description: RegisterManagerSimple
 * @createTime 2022/12/13 23:44:00
 */

public class RegisterManagerSimple {
	public static void main(String[] args) throws Exception {
		String url = "http://10.41.0.190:18500/v1/agent/service/register";
		System.out.println(RegisterManager.register(url, getServerRegistration()));
	}
	public static ServerRegistration getServerRegistration(){
		ServerRegistration registration = new ServerRegistration();
		List<String> tags = new ArrayList<>();
		List<ServerCheck> checks = new ArrayList<>();
		ServerCheck serverCheck = new ServerCheck();
		serverCheck.setHttp("http://192.168.1.6:8901/");
		tags.add("service");
		registration.setId("test")
				.setName("test")
				.setAddress("192.168.1.6")
				.setPort(8901)
				.setTags(tags)
				.setMeta(new ServerMeta())
				.setChecks(checks);
		return null;
	}
}
