package org.letter.springboot;

import org.letter.springboot.context.SpringContextUtil;
import org.letter.springboot.metrics.MetricsAutoRegistration;
import org.letter.springboot.metrics.MetricsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * CommonAutoConfiguration
 *
 */
@Configuration
public class CommonAutoConfiguration {
	@Autowired
	private MetricsConfig config;
    @Bean
    public SpringContextUtil getSpringContextUtil(){
        return new SpringContextUtil();
    }

	@Bean
	public MetricsConfig metricsConfig(){
		MetricsConfig metricsConfig = new MetricsConfig();
		return metricsConfig;
	}
	@Bean
	public MetricsAutoRegistration getMetricsAutoRegistration(){
		MetricsAutoRegistration registration = new MetricsAutoRegistration();
		registration.registerAndStart(config);
		return registration;
	}
}
