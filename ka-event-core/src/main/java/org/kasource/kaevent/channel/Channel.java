package org.kasource.kaevent.channel;

import java.util.Collection;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;
import java.util.Set;

import org.kasource.kaevent.event.filter.EventFilter;


/**
 * The channel is a "Publish / Subscribe Channel" which sends events to all its listeners.
 * 
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
    
    /**
     * Register a new listener object to this channel with specific filters for that particular listener.
     * 
     * @param listenerRegistration
     *            Listener registration object to register
     **/
    public abstract void registerListener(EventListener listener, List<EventFilter<EventObject>> filters);
    
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
   
    /**
     * Register a filter which will invoked on all events routed
     * to this channel. Returns true if filter added else false.
     * 
     * Will only add the filter if it is relevant to the events registered
     * to the channel.
     * 
     * @param filter	Filter to add to the channel filters list.
     * 
     * @return Returns true if filter added else false.
     **/
    public boolean registerFilter(EventFilter<EventObject> filter);
   
}