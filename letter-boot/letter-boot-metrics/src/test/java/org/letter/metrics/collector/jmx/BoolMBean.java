package org.letter.metrics.collector.jmx;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;

public interface BoolMBean {
    public boolean getTrue();
    public boolean getFalse();
}

class Bool implements BoolMBean {

    public static void registerBean(MBeanServer mbs)
            throws JMException {
        ObjectName mbeanName = new ObjectName("boolean:Type=Test");
        Bool mbean = new Bool();
        mbs.registerMBean(mbean, mbeanName);
    }

    public boolean getTrue() {
        return true;
    }

    public boolean getFalse() {
        return false;
    }
}

