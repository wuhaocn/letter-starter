

package org.letter.common.store;

import org.letter.common.extension.SPI;

import java.util.Map;

@SPI("simple")
public interface DataStore {

    /**
     * return a snapshot value of componentName
     */
    Map<String, Object> get(String componentName);

    Object get(String componentName, String key);

    void put(String componentName, String key, Object value);

    void remove(String componentName, String key);

}
