package org.letter.sentinel.provider.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ProviderController
 *
 * @author wuhao
 * @date 2022/10/25
 */

@RestController
public class ProviderController {

	@GetMapping("/test")
	public String test() throws Exception {
		return "OK";
	}

	@GetMapping("/demo")
	public String demo(@RequestParam(required = false) long t) throws Exception {
		return "OK";
	}

}
