package org.kasource.kaevent.channel;

import java.util.EventObject;
import java.util.List;

import org.kasource.kaevent.event.filter.EventFilter;


/**
 * The channel is a "Publish / Subscribe Channel" which sends events to all its listeners.
 * 
 * Channels allows listeners to listen to all types of events (registered at the
 * channel) without knowing the events source.
 * 
 * When you register a EventListener object to the channel, all event listener
 * interfaces that that class implements (which also are registered at the
 * channel) will be registered to listen to.
 * 
 * @author rikard
 * @version $Id$
 **/
public interface ListenerChannel extends Channel {
   
   
    /**
     * Register a new listener object to this channel with specific filters for that particular listener.
     * 
     * @param listener
     *            Listener object.
     * @param  filters
     *             List of event filters.            
     **/
    public abstract void registerListener(Object listener, List<EventFilter<? extends EventObject>> filters);
    
    /**
     * Register a new listener object to this channel.
     * 
     * @param listener
     *            Listener  object to register
     **/
    public abstract void registerListener(Object listener);

    /**
     * Remove a registered listener from this channel.
     * 
     * @param listener
     *            Listener object to unregister
     **/
    public abstract void unregisterListener(Object listener);  
   
}
