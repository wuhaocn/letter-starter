package org.letter.rpc.dubbo.filter;

import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.letter.metrics.PerfmonCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.dubbo.common.constants.CommonConstants.CONSUMER;
import static org.apache.dubbo.common.constants.CommonConstants.PROVIDER;


/**
 * @author wuhao
 */
@Activate(group = {CONSUMER, PROVIDER}, order = 1)
public class DubboMetricsFilter implements Filter {
	protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private static final String METRICS_NAME = "dubbo";
	private static final String METRICS_SUCCESS = "success";
	private static final String METRICS_FAIL = "fail";

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		try {
			String service = invocation.getServiceName().replace(".", "_");
			String method = invocation.getMethodName();
			try (PerfmonCounter.Timer.Context timer = PerfmonCounter.timer(METRICS_NAME, service,
					method, METRICS_SUCCESS).time()) {
				return invoker.invoke(invocation);
			} catch (Exception e){
				PerfmonCounter.meter(METRICS_NAME, service,
						method, METRICS_FAIL).mark();
			}
		} finally {

		}
		return invoker.invoke(invocation);
	}
}
