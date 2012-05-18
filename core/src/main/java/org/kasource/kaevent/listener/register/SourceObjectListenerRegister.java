package org.kasource.kaevent.listener.register;

import java.util.EventObject;
import java.util.List;

import org.kasource.kaevent.event.filter.EventFilter;

/**
 * Register of all source object listeners.
 * 
 * @author rikardwi
 **/
public interface SourceObjectListenerRegister extends EventListenerRegister {

    /**
     * Register a listener to listen on events from sourceObject.
     * 
     * @param listener
     *            Listener to register
     * @param sourceObject
     *            Source object to listen on
     **/
    public abstract void registerListener(Object listener, Object sourceObject);

    /**
     * Register a listener to listen on events from sourceObject.
     * 
     * @param listener
     *            Listener to register
     * @param sourceObject
     *            Source object to listen on
     * @param filters
     *            Filters to be applied on all events passed to this listener.
     **/
    public abstract void registerListener(Object listener, Object sourceObject,
                List<EventFilter<? extends EventObject>> filters);

    /**
     * Unregister a listener to listen on events from sourceObject.
     * 
     * @param listener
     *            Listener to unregister
     * @param sourceObject
     *            Source object to unregister from
     **/
    public abstract void unregisterListener(Object listener, Object sourceObject);

}
