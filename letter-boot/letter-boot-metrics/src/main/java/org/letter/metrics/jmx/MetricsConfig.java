package org.letter.metrics.jmx;

/**
 * Metrics 的 配套属性
 *
 * @author letter
 **/
public class MetricsConfig implements MetricsConfigMBean {
    private boolean needMetrics =true;
    private boolean needJvmMetrics = true;

    @Override
    public boolean needMetrics() {
        return needMetrics;
    }

    @Override
    public void setNeedMetrics(boolean isNeedMetrics) {
        this.needMetrics = isNeedMetrics;
    }

    @Override
    public boolean needJvmMetrics() {
        return needJvmMetrics;
    }

    @Override
    public void setNeedJvmMetrics(boolean isNeedJvmMetrics) {
        this.needJvmMetrics = isNeedJvmMetrics;
    }
}
