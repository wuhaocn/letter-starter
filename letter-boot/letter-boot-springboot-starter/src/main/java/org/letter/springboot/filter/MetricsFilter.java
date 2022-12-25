package org.letter.springboot.filter;

import org.letter.metrics.PerfmonCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/**
 * <filter>
 * <filter-name>metricsFilter</filter-name>
 * <filter-class>org.letter.springboot.filfter.MetricsFilter</filter-class>
 * </filter>
 * <filter-mapping>
 * <filter-name>metricsFilter</filter-name>
 * <url-pattern>/*</url-pattern>
 * </filter-mapping>
 * @author wuhao
 */
public class MetricsFilter implements Filter {
	private static final String METRICS_NAME = "http";
	private static final String METRICS_SUCCESS = "success";
	private static final String METRICS_FAIL = "fail";
	private static final Logger LOGGER = LoggerFactory.getLogger(MetricsFilter.class);

	/**
	 * 容器加载的时候调用
	 *
	 * @param filterConfig
	 * @throws ServletException
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}


	/**
	 * 请求被拦截的时候进行调用
	 *
	 * @param servletRequest
	 * @param servletResponse
	 * @param filterChain
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) servletRequest;
		String url = req.getRequestURI().replace("/", "");
		try {
			try(PerfmonCounter.Timer.Context timer = PerfmonCounter.timer(METRICS_NAME, METRICS_NAME, url, METRICS_SUCCESS).time()) {
				filterChain.doFilter(servletRequest, servletResponse);
			}
		} catch (Exception e){
			PerfmonCounter.meter(METRICS_NAME, METRICS_NAME, url, METRICS_FAIL).mark();
			LOGGER.error("request error:{}", servletRequest.getRemoteAddr(), e);
			throw e;
		}

	}

	/**
	 * 容器被销毁的时候被调用
	 */
	@Override
	public void destroy() {

	}

}
