package org.letter.metrics.jmx;

/**
 * Metrics 的 配套属性
 *
 * @author letter
 **/
public interface MetricsServiceInfoMBean {

	/**
	 * getClusterName
	 * @return
	 */
	String getClusterName();

	/**
	 * getExclusiveId
	 * 
	 * @return
	 */
	String getExclusiveId();

	/**
	 * getNodeName
	 * @return
	 */
	String getNodeName();

	/**
	 * getNodeName
	 * @return
	 */
	String getPid();

}
