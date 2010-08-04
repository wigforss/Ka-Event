package org.kasource.kaevent.listener.register;

import java.util.EventListener;
import java.util.EventObject;
import java.util.Set;


public interface ChannelListenerRegister extends EventListenerRegister{
    /**
     * Register a new listener object to this channel
     * 
     * @param listener
     *            Listener object to register
     **/
    public abstract void registerListener(EventListener listener);
    
    

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
