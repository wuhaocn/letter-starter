
package org.letter.event;

/**
 * Generic {@link Event letter event}
 *
 * @param <S> the type of event source
 * @since 2.7.5
 */
public class GenericEvent<S> extends Event {

    public GenericEvent(S source) {
        super(source);
    }

    public S getSource() {
        return (S) super.getSource();
    }
}
