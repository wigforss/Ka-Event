package org.kasource.kaevent.listener.register;

import java.util.EventListener;
import java.util.EventObject;
import java.util.List;

import org.kasource.kaevent.event.filter.EventFilter;



public interface SourceObjectListenerRegister extends EventListenerRegister{

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
         * Register a listener to listen on events from sourceObject
         * 
         * @param listener
         *            Listener to register
         * @param sourceObject
         *            Source object to listen on
         **/
        public abstract void registerListener(EventListener listener,
                        Object sourceObject, List<EventFilter<EventObject>> filters);

	
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
	
	 
}