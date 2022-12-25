package org.letter.springboot;

import org.letter.springboot.context.SpringContextUtil;
import org.letter.springboot.filter.MetricsFilter;
import org.letter.springboot.metrics.MetricsAutoRegistration;
import org.letter.springboot.metrics.MetricsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.Arrays;

/**
 * CommonAutoConfiguration
 *
 * @author wuhao
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
		registration.registerAndStarter(config);
		return registration;
	}

	@Bean
	public FilterRegistrationBean<Filter> getMetricsFilter(){
		FilterRegistrationBean<Filter> filterRegistration = new FilterRegistrationBean();
		MetricsFilter metricsFilter = new MetricsFilter();
		filterRegistration.setFilter(metricsFilter);
		filterRegistration.setUrlPatterns(Arrays.asList("/*"));
		filterRegistration.setOrder(1);
		return filterRegistration;
	}
}
