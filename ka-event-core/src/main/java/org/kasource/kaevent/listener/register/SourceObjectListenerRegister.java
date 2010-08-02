package org.kasource.kaevent.listener.register;

import java.util.EventListener;
import java.util.EventObject;
import java.util.Set;



public interface SourceObjectListenerRegister {

	/**
	 * Register a listener to listen on events from sourceObject
	 * 
	 * @param listener
	 *            Listener to register
	 * @param sourceObject
	 *            Source object to listen on
	 **/
	public abstract void registerListener(EventListener listener,
			Object sourceObject);

	/**
	 * Unregister a listener to listen on events from sourceObject
	 * 
	 * @param listener
	 *            Listener to unregister
	 * @param sourceObject
	 *            Source object to unregister from
	 **/
	public abstract void unregisterListener(EventListener listener,
			Object sourceObject);
	
	 /**
     * Returns the set of listener objects that listens to the event class
     * <i>eventClass</i>.
     * 
     * @param eventClass
     *            The event class to find listener object for.
     * 
     * @return all listener object of eventClass
     **/
    public abstract Set<EventListener> getListenersByEvent(EventObject event);

}