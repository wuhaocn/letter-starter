
package org.letter.common.config;


import org.letter.common.extension.SPI;

import java.util.Properties;

/**
 * 
 * The smaller value, the higher priority
 * 
 */
@SPI
public interface OrderedPropertiesProvider {
    /**
     * order
     *
     * @return
     */
    int priority();

    /**
     * load the properties
     *
     * @return
     */
    Properties initProperties();
}
