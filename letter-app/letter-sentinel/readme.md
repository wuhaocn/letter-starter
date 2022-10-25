## 概述
* 仓库地址
  https://github.com/alibaba/Sentinel
* 使用介绍

## 引入依赖
```
//log4j
compile "org.slf4j:slf4j-api:${slf4j_version}"
//spring
compile group: 'org.springframework.boot', name: 'spring-boot-starter-web', version: springboot_version
compile group: 'org.springframework.boot', name: 'spring-boot-starter-aop', version: springboot_version
compile group: 'org.springframework.boot', name: 'spring-boot-starter-actuator', version: springboot_version

//sentinel
// https://mvnrepository.com/artifact/com.alibaba.csp/sentinel-core
implementation group: 'com.alibaba.csp', name: 'sentinel-core', version: '1.8.5'
```

## 使用
### simple使用-SphU.entry

```
@GetMapping("/lpro")
public String apiLPro(@RequestParam(required = false) String app) throws Exception {
    return providerService.callServerLimit(app);
}

```

```
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

```

