
package org.letter.common.status;

import org.letter.common.extension.SPI;

/**
 * StatusChecker
 */
@SPI
public interface StatusChecker {

    /**
     * check status
     *
     * @return status
     */
    Status check();

}