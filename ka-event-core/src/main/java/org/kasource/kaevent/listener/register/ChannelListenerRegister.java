package org.kasource.kaevent.listener.register;

import java.util.EventListener;
import java.util.EventObject;
import java.util.List;
import java.util.Set;

import org.kasource.kaevent.event.filter.EventFilter;


public interface ChannelListenerRegister extends EventListenerRegister{
    /**
     * Register a new listener object to this channel
     * 
     * @param listener
     *            Listener object to register
     **/
    public abstract void registerListener(EventListener listener);
    
    
    /**
     * Register a new listener object to this channel
     * 
     * @param listener
     *            Listener object to register
     **/
    public void registerListener(EventListener listener, List<EventFilter<EventObject>> filters);
    
    /**
     * Remove a registered listener from this channel
     * 
     * @param listener
     *            Listener object to unregister
     **/
    public abstract void unregisterListener(EventListener listener);
    
    /**
     * Refresh listeners (listenersByEvent). Will find listeners that listens on
     * eventClass but is currently not registered as listener on that class, due
     * to the fact that the listeners has been registered before the eventClass.
     * 
     * @param eventClass
     **/
    public abstract void refreshListeners(Class<? extends EventObject> eventClass);
    
   
}
