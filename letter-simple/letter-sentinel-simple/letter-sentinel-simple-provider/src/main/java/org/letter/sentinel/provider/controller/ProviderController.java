package org.letter.sentinel.provider.controller;

import org.letter.sentinel.provider.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private ProviderService providerService;

	@GetMapping("/pro")
	public String apiPro(@RequestParam(required = false) long t) throws Exception {
		return providerService.callServer(t);
	}
	@GetMapping("/lpro")
	public String apiLPro(@RequestParam(required = false) String app) throws Exception {
		return providerService.callServerLimit(app);
	}

}
