package org.letter.metrics.utils;

import java.util.Arrays;
import java.util.List;

/**
 * MetricsConstant
 *
 * @author letter
 */
public class MetricsConstant {
    private MetricsConstant() { }


	public static final String SEP = "_";

	public static final String METRICS = "metrics";
	public static final String METRICS_JMX = "metrics:*";
	public static final String METRICS_CONFIG = "metrics:name=MetricsConfig";
	public static final String METRICS_SERVICE = "metrics:name=MetricsServiceInfo";
	/**
	 * APP 服务及节点归类
	 */
	public static final String APP = "app";
	public static final String NAME = "name";
	//metrics<name=jvm_thread_states.blocked.count, type=gauges><>Value: 0
	//name:jvm_thread_states  method:blocked item:count 拆解
	//name:database  sign:app/config

	//整体规则item(L1)_method_sign_ext1_ext2_ext3
	/**
	 * ITEM L1 rpc类别/数据库实例/redis实例
	 */
	public static final String ITEM = "item";

	/**
	 * METHOD L2 登录发消息/读/写
	 */
	public static final String METHOD = "method";

	/**
	 * SIGN L3 特殊标识类
	 */
	public static final String SIGN = "sign";




	///type
	public static final String GAUGE = "gauge";
	public static final String TIMER = "timer";
	public static final String COUNTER = "counter";
	public static final String METER = "meter";
	public static final String HISTOGRAM = "histogram";

	///sign
    public static final String CAPACITY = "capacity";
	public static final String VALUE = "value";
	public static final String NUMBER = "number";
    public static final String COUNT = "count";
    public static final String SIZE = "size";
    public static final String IN = "in";
    public static final String OUT = "out";
	public static final String SUBMIT = "submit";
    public static final String SUCCESS = "success";
	public static final String FAIL = "fail";
    public static final String TIMEOUT = "timeout";

	/**
	 * 调用指标
	 */
	public static final String RPC = "rpc";
	/**
	 * redis指标
	 */
	public static final String REDIS = "redis";
	/**
	 * 数据库指标
	 */
	public static final String DATABASE = "database";
	/**
	 * kafka指标
	 */
	public static final String KAFKA = "kafka";
	/**
	 * hbase指标
	 */
	public static final String HBASE = "hbase";
	/**
	 * 队列指标
	 */
	public static final String QUEUE = "queue";
	/**
	 * 服务指标
	 */
	public static final String SERVICE = "service";
	/**
	 * 缓存指标
	 */
	public static final String CACHE = "cache";
	/**
	 * 日志指标
	 */
	public static final String LOG = "log";

}
