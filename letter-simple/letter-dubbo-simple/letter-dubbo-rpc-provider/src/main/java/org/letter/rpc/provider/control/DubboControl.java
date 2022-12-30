package org.letter.rpc.provider.control;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.spring.boot.autoconfigure.DubboConfigurationProperties;
import org.letter.rpc.api.MessageRequest;
import org.letter.rpc.api.MessageResponse;
import org.letter.rpc.api.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.UUID;

@RestController
public class DubboControl {

	//注册中心负载-dubbo
	@DubboReference(protocol = "dubbo", check = false)
	private MessageService messageServiceDubbo;


	@Autowired
	private DubboConfigurationProperties properties;

	@GetMapping("/dubbo/test")
	@ResponseBody
	public MessageResponse doTestDubbo() {
		MessageRequest messageRequest = new MessageRequest();
		messageRequest.setTid(UUID.randomUUID().toString());
		System.out.println("call send for dubbo");
		MessageResponse messageResponse = messageServiceDubbo.send(messageRequest);
		return messageResponse;
	}


//	@GetMapping("/letter/test")
//	@ResponseBody
//	public MessageResponse doTest(){
//		MessageRequest messageRequest = new MessageRequest();
//		messageRequest.setTid(UUID.randomUUID().toString());
//		System.out.println("call send for letter");
//		MessageResponse messageResponse = messageServiceletter.send(messageRequest);
//		return messageResponse;
//	}
}
