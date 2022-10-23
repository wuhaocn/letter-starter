package org.letter.common;

import org.letter.common.extension.SPI;

/**
 * @author nick
 **/
@SPI
public interface ByteDecode {
    byte[] decode(byte[] bytes) throws Exception;
}
