package org.letter.perform.register;

import org.letter.perform.utils.JsonUtil;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * RegisterManager
 *
 * @author wuhao
 * @description: RegisterManager
 * @createTime 2022/11/15 16:48:00
 */

public class RegisterManager {
	public static void main(String[] args) throws Exception {
		String url = "http://127.0.0.1:18500/v1/agent/service/register";
		System.out.println(register(url));
	}

	public static String register(String addr) throws Exception {
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
		String content = JsonUtil.toJson(registration).toString();
		return put(addr, content);
	}

	public static String put(String addr, String content) throws Exception {

		try {
			URL url = new URL(addr);
			URLConnection urlConnection = url.openConnection();
			HttpURLConnection connection = (HttpURLConnection) urlConnection;
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			connection.setRequestMethod("PUT");
			connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			connection.setRequestProperty("Accept", "*/*");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setDoOutput(true);
			OutputStream os = connection.getOutputStream();
			os.write(content.getBytes(StandardCharsets.UTF_8));
			os.flush();
			os.close();

			InputStream inputStream = connection.getInputStream();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, len);
			}
			inputStream.close();
			outputStream.close();
			String response = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
			return response;
		} catch (Exception e) {
			throw e;
		}
	}
}

/**
 curl -X PUT -d '{"id": "test1","name": "test1","address": "192.168.56.12","port": 9100,"tags": ["service"],"checks": [{"http": "http://192.168.56.12:9100/","interval": "5s"}]}'
 http://127.0.0.1:8500/v1/agent/service/unregister


 */