package org.kasource.kaevent.core.channel;

import java.util.EventListener;
import java.util.EventObject;
import java.util.List;
import java.util.Set;

import org.kasource.kaevent.core.filter.EventFilter;
import org.kasource.kaevent.core.filter.EventFilterRegister;
import org.kasource.kaevent.core.listener.register.ChannelListenerRegister;


/**
 * Channels allows listeners to listen to all types of events (registered at the
 * channel) without knowing the events source.
 * 
 * When register a EventListener object to the channel, all event listener
 * interfaces that that class implements (which also are registered at the
 * channel) will be registered to listen to.
 * 
 * @author rikard
 * @version $Id$
 **/
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
     * Register a new Event type to the register. Any listener already registered
     * that implements its listener will not be notified, thus that listener
     * needs to re-register to get events of this new type.
     * 
     * @param eventClass
     *            New event type to handle
     **/
    public abstract void registerEvent(Class<? extends EventObject> eventClass);
    
    
    /**
     * Register a new listener object to this channel with specific filters for that particular listener.
     * 
     * @param listenerRegistration
     *            Listener registration object to register
     **/
    public abstract void registerListener(EventListener listener, List<EventFilter<? extends EventObject>> filters);
    
    /**
     * Register a new listener object to this channel
     * 
     * @param listener
     *            Listener  object to register
     **/
    public abstract void registerListener(EventListener listener);

    /**
     * Remove a registered listener from this channel
     * 
     * @param listener
     *            Listener object to unregister
     **/
    public abstract void unregisterListener(EventListener listener);
    
   
}