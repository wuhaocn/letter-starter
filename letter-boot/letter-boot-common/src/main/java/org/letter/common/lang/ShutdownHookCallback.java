
package org.letter.common.lang;

import org.letter.common.extension.SPI;

/**
 * letter ShutdownHook callback interface
 *
 * @since 2.7.5
 */
@SPI
public interface ShutdownHookCallback extends Prioritized {

    /**
     * Callback execution
     *
     * @throws Throwable if met with some errors
     */
    void callback() throws Throwable;
}
