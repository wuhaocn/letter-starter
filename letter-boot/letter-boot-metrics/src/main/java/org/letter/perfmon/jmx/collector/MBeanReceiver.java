package org.letter.perfmon.jmx.collector;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * MBeanReceiver
 *
 * @author letter
 */
public interface MBeanReceiver {
	/**
	 * recordBean
	 *
	 * @param domain
	 * @param beanProperties
	 * @param attrKeys
	 * @param attrName
	 * @param attrType
	 * @param attrDescription
	 * @param value
	 */
	void recordBean(
			String domain,
			LinkedHashMap<String, String> beanProperties,
			LinkedList<String> attrKeys,
			String attrName,
			String attrType,
			String attrDescription,
			Object value);
}