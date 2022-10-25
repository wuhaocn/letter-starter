package org.letter.sentinel.provider.service;

/**
 * ProviderController
 *
 * @author wuhao
 * @date 2022/10/25
 */

public interface ProviderService {

	/**
	 * callServer
	 *
	 * @param s
	 * @return
	 */
    String callServer(long s);

	/**
	 * callServerLimit
	 *
	 * @param app
	 * @return
	 */
	String callServerLimit(String app);

}
