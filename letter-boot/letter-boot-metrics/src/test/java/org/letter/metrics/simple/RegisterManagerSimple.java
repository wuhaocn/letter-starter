package org.letter.metrics.simple;

import org.letter.metrics.register.RegisterManager;
import org.letter.metrics.register.ServerCheck;
import org.letter.metrics.register.ServerMeta;
import org.letter.metrics.register.ServerRegistration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wuhao
 * @description: RegisterManagerSimple
 * @createTime 2022/12/13 23:44:00
 */

public class RegisterManagerSimple {
	public static void main(String[] args) throws Exception {
		String url = "http://127.0.0.1:18500/v1/agent/service/register";
		//RegisterManager.register(url, getServerRegistration());
		RegisterManager.put(url, test);
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

	public static String test = "{\n" +
			"  \"ID\": \"redis1\",\n" +
			"  \"Name\": \"redis\",\n" +
			"  \"Tags\": [\"primary\", \"v1\"],\n" +
			"  \"Address\": \"127.0.0.1\",\n" +
			"  \"Port\": 8000,\n" +
			"  \"Meta\": {\n" +
			"    \"redis_version\": \"4.0\"\n" +
			"  },\n" +
			"  \"EnableTagOverride\": false,\n" +
			"  \"Check\": {\n" +
			"    \"DeregisterCriticalServiceAfter\": \"1m\",\n" +
//			"    \"Interval\": \"10s\",\n" +
			"     \"ttl\": \"10s\"," +
			"    \"Timeout\": \"5s\"\n" +
			"  },\n" +
			"  \"Weights\": {\n" +
			"    \"Passing\": 10,\n" +
			"    \"Warning\": 1\n" +
			"  }\n" +
			"}\n";
}
