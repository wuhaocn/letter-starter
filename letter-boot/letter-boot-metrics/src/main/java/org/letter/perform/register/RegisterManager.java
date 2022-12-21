package org.letter.perform.register;

import org.letter.perform.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * RegisterManager
 * refer: https://www.consul.io/api-docs/agent/service#register-service
 * @author wuhao
 * @description: RegisterManager
 * @createTime 2022/11/15 16:48:00
 */

public class RegisterManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(RegisterManager.class);
	private AtomicBoolean start = new AtomicBoolean(false);
	private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

	public void start(String addr, ServerRegistration registration){
		if (start.compareAndSet(false, true)){
			scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					try {
						String content = JsonUtil.toJson(registration).toString();
						put(addr, content);
					} catch (Exception e) {
						LOGGER.warn("register error:{}", addr);
					}
				}
			}, 10, 10, TimeUnit.SECONDS);
		}
	}
	public static void register(String addr, ServerRegistration registration) throws Exception {
		RegisterManager registerManager = new RegisterManager();
		registerManager.start(addr, registration);
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