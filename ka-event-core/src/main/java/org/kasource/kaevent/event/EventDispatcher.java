/**
 * 
 */
package org.kasource.kaevent.event;

import java.util.EventListener;
import java.util.EventObject;
import java.util.List;

import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.event.filter.EventFilter;

/**
 * Used to dispatch events, creating channels and registering listeners.
 * 
 * 
 * @author rikardwigforss
 * @version $Id$
 */
public interface EventDispatcher {
    /**
     * Fire event and queue it for later delivery and return.
     * 
     * @param event
     *            Event to fire
     **/
    public void fire(EventObject event);

    
    /**
     * Fire event and block until all listeners have processed the event.
     * 
     * @param event
     *            Event to fire
     **/
    public void fireBlocked(EventObject event);
    
    /**
     * Fire event when the current thread's transaction is successfully committed.
     * 
     * NOTE: May not be implemented by all implementations
     * 
     * @param event
     *            Event to fire
     */
    public void fireOnCommit(EventObject event);
    
    /**
     * Creates and new channel.
     * 
     * @param channelName Name of the channel to create.
     * 
     * @return Channel created.
     **/
    public Channel createChannel(String channelName);
    
    /**
     * Returns a named channel.
     * 
     * @param channelName Name of channel.
     * 
     * @return named channel.
     **/
    public Channel getChannel(String channelName);
     
    /**
     * Register listener as a listener to events with sourceObject as
     * the event source.
     * 
     * @param listener      Listener to register.
     * @param sourceObject  Object to listen on.
     **/
    public void registerListener(EventListener listener,
            Object sourceObject);
    
    /**
     * Unregister listener from sourceObject as
     * the event source.
     * 
     * @param listener      Listener to unregister.
     * @param sourceObject  Object to stop listen to.
     **/
    public void unregisterListener(EventListener listener,
            Object sourceObject);
    
    /**
     * Register listener as a listener to events with sourceObject as
     * the event source.
     * 
     * @param listener      Listener to register.
     * @param sourceObject  Object to listen on.
     * @param filters       List of filters to apply on events dispatched to the listener.
     **/
    public void registerListener(EventListener listener,
                Object sourceObject, List<EventFilter<EventObject>> filters);
    
    /**
     * Register listener as a Channel listener of the named channel.
     * 
     * @param listener      Listener to register.
     * @param channelName   Name of the channel to listen on.
     * 
     * @throws IllegalArgumentException if the named Channel is not
     * a ListenerChannel. 
     **/
    public void registerListenerAtChannel(EventListener listener,
            String channelName) throws IllegalArgumentException;
    
    
    /**
     * Unregister listener listener from the named channel.
     * 
     * @param listener      Listener to unregister.
     * @param channelName   Name of the channel to stop listen to.
     * 
     * @throws IllegalArgumentException if the named Channel is not
     * a ListenerChannel. 
     **/
    public void unregisterListenerFromChannel(EventListener listener,
            String channelName) throws IllegalArgumentException;
    
    /**
     * Register listener as a Channel listener of the named channel.
     * 
     * @param listener      Listener to register.
     * @param channelName   Name of the channel to listen on.
     * @param filters       List of filters to apply on events dispatched to the listener.
     * 
     * @throws IllegalArgumentException if the named Channel is not
     * a ListenerChannel. 
     **/
    public void registerListenerAtChannel(EventListener listener,
            String channelName, List<EventFilter<EventObject>> filters) throws IllegalArgumentException;
    
    
    /**
     * Add event to this threads batch.
     * 
     * @param event Event to add.
     **/
    public void addToBatch(EventObject event);
    
    /**
     * Clear this threads batch.
     **/
    public void clearBatch();
    
    /**
     * Fire all events in this threads batch.
     **/
    public void fireBatch();
   
    
}
