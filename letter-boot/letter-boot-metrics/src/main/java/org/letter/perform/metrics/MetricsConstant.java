package org.letter.perform.metrics;

/**
 *
 **/
public class MetricsConstant {
    private MetricsConstant() { }

    public static final String NAME_CAPACITY = "capacity";
    public static final String NAME_COUNT = "count";
    public static final String NAME_SIZE = "size";
    public static final String NAME_IN = "in";
    public static final String NAME_OUT = "out";
    public static final String NAME_FAIL = "fail";
    public static final String NAME_SUCC = "succ";
    public static final String NAME_TIMEOUT = "timeout";

    public static final String GAUGE = "gauge";
    public static final String TIMER = "timer";
    public static final String COUNTER = "counter";
    public static final String METER = "meter";
    public static final String HISTOGRAM = "histogram";

    public static final String PREFIX_HTTP = "exporter";
    public static final String PREFIX_RPC = "rpc";
    public static final String PREFIX_THREAD_POOL = "threadPool";
    public static final String PREFIX_QUEUE = "queue";

	public static final String NAME_DB = "database";

    public static final String NAME_THREAD_SUBMIT = "submit";
    public static final String NAME_THREAD_SUBMIT_FAIL = "submitFail";
    public static final String NAME_THREAD_EXEC = "exec";
    public static final String NAME_THREAD_EXEC_EXCEPTION = "exception";
    public static final String NAME_THREAD_CORE_SIZE = "coreSize";
    public static final String NAME_THREAD_ACTIVE_COUNT = "activeCount";
    public static final String NAME_THREAD_POOL_SIZE = "poolCount";


}
