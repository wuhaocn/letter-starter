package org.letter.perform.jmx.metrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * MetricsConstant
 *
 * @author letter
 */
public class MetricsConstant {
    private MetricsConstant() { }

	/**
	 * 组合指标
	 */
	public static final List<String> SIMPLE = Arrays.asList("jvm.memory.pools",
			"jvm.thread", "jvm.gc", "jvm.fd.usage", "jvm.memory.total");
	///type
	public static final String GAUGE = "gauge";
	public static final String TIMER = "timer";
	public static final String COUNTER = "counter";
	public static final String METER = "meter";
	public static final String HISTOGRAM = "histogram";

	///sign
    public static final String CAPACITY = "capacity";
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
