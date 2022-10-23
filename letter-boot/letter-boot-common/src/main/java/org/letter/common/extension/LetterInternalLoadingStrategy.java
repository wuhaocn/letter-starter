
package org.letter.common.extension;

/**
 * letter internal {@link LoadingStrategy}
 *
 * @since 2.7.7
 */
public class LetterInternalLoadingStrategy implements LoadingStrategy {

    @Override
    public String directory() {
        return "META-INF/letter/internal/";
    }

    @Override
    public int getPriority() {
        return MAX_PRIORITY;
    }
}
