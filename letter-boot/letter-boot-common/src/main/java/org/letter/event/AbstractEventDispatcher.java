
package org.letter.event;

import org.letter.common.extension.ExtensionLoader;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Collections.sort;
import static java.util.Collections.unmodifiableList;

/**
 * The abstract {@link EventDispatcher} providers the common implementation.
 *
 * @see EventDispatcher
 * @see Listenable
 * @see ServiceLoader
 * @see EventListener
 * @see Event
 * @since 2.7.5
 */
public abstract class AbstractEventDispatcher implements EventDispatcher {

    private final Object mutex = new Object();

    private final ConcurrentMap<Class<? extends Event>, List<EventListener>> listenersCache = new ConcurrentHashMap<>();

    private final Executor executor;

    /**
     * Constructor with an instance of {@link Executor}
     *
     * @param executor {@link Executor}
     * @throws NullPointerException <code>executor</code> is <code>null</code>
     */
    protected AbstractEventDispatcher(Executor executor) {
        if (executor == null) {
            throw new NullPointerException("executor must not be null");
        }
        this.executor = executor;
        this.loadEventListenerInstances();
    }

    @Override
    public void addEventListener(EventListener<?> listener) throws NullPointerException, IllegalArgumentException {
        //assertListener(listener);
        doInListener(listener, listeners -> {
            addIfAbsent(listeners, listener);
        });
    }

    @Override
    public void removeEventListener(EventListener<?> listener) throws NullPointerException, IllegalArgumentException {
        //        assertListener(listener);
        doInListener(listener, listeners -> listeners.remove(listener));
    }

    @Override
    public List<EventListener<?>> getAllEventListeners() {
        List<EventListener<?>> listeners = new LinkedList<>();

        sortedListeners().forEach(listener -> {
            addIfAbsent(listeners, listener);
        });

        return unmodifiableList(listeners);
    }

    protected Stream<EventListener> sortedListeners() {
        return sortedListeners(e -> true);
    }

    protected Stream<EventListener> sortedListeners(Predicate<Map.Entry<Class<? extends Event>, List<EventListener>>> predicate) {
        return listenersCache
                .entrySet()
                .stream()
                .filter(predicate)
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .sorted();
    }

    private <E> void addIfAbsent(Collection<E> collection, E element) {
        if (!collection.contains(element)) {
            collection.add(element);
        }
    }

    @Override
    public void dispatch(Event event) {

        Executor executor = getExecutor();

        // execute in sequential or parallel execution model
        executor.execute(() -> {
            sortedListeners(entry -> entry.getKey().isAssignableFrom(event.getClass()))
                    .forEach(listener -> {
                        if (listener instanceof ConditionalEventListener) {
                            ConditionalEventListener predicateEventListener = (ConditionalEventListener) listener;
                            if (!predicateEventListener.accept(event)) { // No accept
                                return;
                            }
                        }
                        // Handle the event
                        listener.onEvent(event);
                    });
        });
    }

    /**
     * @return the non-null {@link Executor}
     */
    @Override
    public final Executor getExecutor() {
        return executor;
    }

    protected void doInListener(EventListener<?> listener, Consumer<Collection<EventListener>> consumer) {
        Class<? extends Event> eventType = EventListener.findEventType(listener);
        if (eventType != null) {
            synchronized (mutex) {
                List<EventListener> listeners = listenersCache.computeIfAbsent(eventType, e -> new LinkedList<>());
                // consume
                consumer.accept(listeners);
                // sort
                sort(listeners);
            }
        }
    }

    /**
     * Default, load the instances of {@link EventListener event listeners} by {@link ServiceLoader}
     * <p>
     * It could be override by the sub-class
     *
     * @see EventListener
     * @see ServiceLoader#load(Class)
     */
    protected void loadEventListenerInstances() {
        ExtensionLoader<EventListener> loader = ExtensionLoader.getExtensionLoader(EventListener.class);
        loader.getSupportedExtensionInstances().forEach(this::addEventListener);
    }
}
