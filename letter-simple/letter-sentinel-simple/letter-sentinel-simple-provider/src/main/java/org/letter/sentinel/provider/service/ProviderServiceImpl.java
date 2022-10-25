package org.letter.sentinel.provider.service;


import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


/**
 * ProviderController
 *
 * @author wuhao
 * @date 2022/10/25
 */
@Service
public class ProviderServiceImpl implements ProviderService {

	@PostConstruct
	public void init(){
		initFlowRules();
	}
	@Override
	public String callServer(long s) {
		return "Date "+ System.currentTimeMillis();
	}

	@Override
	public String callServerLimit(String app) {
		try (Entry entry = SphU.entry(app)) {
		} catch (BlockException ex) {
			// 处理被流控的逻辑
			return "Date Block "+ System.currentTimeMillis();
		}
		return "Date "+ System.currentTimeMillis();
	}
	private static void initFlowRules(){
		List<FlowRule> rules = new ArrayList<>();
		FlowRule rule = new FlowRule();
		rule.setResource("app");
		rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
		// Set limit QPS to 20.
		rule.setCount(2);
		rules.add(rule);
		FlowRuleManager.loadRules(rules);
	}
}
