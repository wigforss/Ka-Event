package org.kasource.kaevent.channel;

import java.util.Collection;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Set;

public interface Channel {
	 /**
     * Fire event synchronously to all channel listeners
     * 
     * @param event     Event to dispatch
     **/
    public  abstract void fireEvent(EventObject event, boolean blocked);


    /**
     * Return the name of the channel.
     * 
     * @return name of the channel
     **/
    public abstract String getName();

    /**
     * Set the name of the channel.
     * 
     * @param name
     *            Name of the channel. Name should be unique within a single
     *            ChannelFactory.
     **/
    public abstract void setName(String name);

    
    /**
     * Returns the set of events this channel handles.
     * 
     * @return all events this channel handles
     **/
    public abstract Set<Class<? extends EventObject>> getEvents();
    
    /**
     * Returns a collection of all Event Listener Interfaces supported by this channel.
     * 
     * @return List of EventListener interfaces supported.
     **/
    public abstract Collection<Class<? extends EventListener>> getSupportedInterfaces();
    
    /**
     * Register a new Event type to the register. Any listener already registered
     * that implements its listener will not be notified, thus that listener
     * needs to re-register to get events of this new type.
     * 
     * @param eventClass
     *            New event type to handle
     **/
    public abstract void registerEvent(Class<? extends EventObject> eventClass);
    
    /**
     * Unregister the event from this channel. 
     * 
     * @param eventClass	Class of the event to unregister.
     **/
    public abstract void unregisterEvent(Class<? extends EventObject> eventClass);
    
}
