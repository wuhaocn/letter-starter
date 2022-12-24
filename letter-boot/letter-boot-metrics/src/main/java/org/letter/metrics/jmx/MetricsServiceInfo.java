package org.letter.metrics.jmx;


import java.lang.management.ManagementFactory;

/**
 * MetricsServiceInfo
 *
 * @author letter
 **/
public class MetricsServiceInfo implements MetricsServiceInfoMBean {
    @Override
    public String getClusterName() {
        return "app";
    }

    @Override
    public String getExclusiveId() {
        return "";
    }

    @Override
    public String getNodeName() {
        return "app";
    }

    @Override
    public String getPid() {
        return ManagementFactory
            .getRuntimeMXBean()
            .getName().split("@")[0];
    }
}
