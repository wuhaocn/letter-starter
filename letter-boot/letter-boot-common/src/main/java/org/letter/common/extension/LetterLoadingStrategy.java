
package org.letter.common.extension;

/**
 * letter {@link LoadingStrategy}
 *
 * @since 2.7.7
 */
public class LetterLoadingStrategy implements LoadingStrategy {

    @Override
    public String directory() {
        return "META-INF/letter/";
    }

    @Override
    public boolean overridden() {
        return true;
    }

    @Override
    public int getPriority() {
        return NORMAL_PRIORITY;
    }


}
