package org.kasource.kaevent.listener.register;

import java.util.Collection;
import java.util.EventObject;
import java.util.List;

import org.kasource.kaevent.event.filter.EventFilter;

/**
 * Channel Listener Register.
 * 
 * @author rikardwi
 **/
public interface ChannelListenerRegister extends EventListenerRegister {
    /**
     * Register a new listener object to this channel.
     * 
     * @param listener
     *            Listener object to register
     **/
    public abstract void registerListener(Object listener);
    
    /**
     * Returns all event listener registration by an event.
     * 
     * @param eventClass Event to get listeners for.
     * 
     * @return All Listeners for an event.
     **/
    public Collection<EventListenerRegistration> getListenersByEvent(Class<? extends EventObject> eventClass);
    
    /**
     * Register a new listener object to this channel.
     * 
     * @param listener
     *            Listener object to register
     * @param filters
     *            Filters to be applied on all events passed to this listener.
     **/
    public void registerListener(Object listener, List<EventFilter<EventObject>> filters);
    
    /**
     * Remove a registered listener from this channel.
     * 
     * @param listener
     *            Listener object to unregister
     **/
    public abstract void unregisterListener(Object listener);
    
    /**
     * Refresh listeners (listenersByEvent). Will find listeners that listens on
     * eventClass but is currently not registered as listener on that class, due
     * to the fact that the listeners has been registered before the eventClass.
     * 
     * @param eventClass Event that has been added to the channel.
     **/
    public abstract void refreshListeners(Class<? extends EventObject> eventClass);
    
   
}
