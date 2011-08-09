package org.kasource.kaevent.channel;

import java.util.EventListener;
import java.util.EventObject;
import java.util.Set;

/**
 * Factory for creating channel instances.
 * 
 * All Channels needs to be registered or created via this factory to be used in the ka-event environment.
 * 
 * @author rikardwigforss
 * @version $Id$
 **/
public interface ChannelFactory {

	/**
	 * Creates and registers a new channel of the default implementation ChannelImpl.
	 * 
	 * @param channelName Name of the new channel
	 * 
	 * @return The newly created channel.
	 **/
	public abstract Channel createChannel(String channelName);
	
	/**
	 * Creates and registers a new channel of the default implementation ChannelImpl.
	 * 
	 * @param channelClass	The channel implementation to use (default is ChannelImpl).
	 * @param channelName 	Name of the new channel
	 * 
	 * @return The newly created channel.
	 **/
    public abstract Channel createChannel(Class<? extends Channel> channelClass, String channelName);

    /**
	 * Creates and registers a new channel of the supplied channel class, registers 
	 * all events.
	 * 
	 * @param channelClass	The channel implementation to use (default is ChannelImpl).
	 * @param channelName 	Name of the new channel.
	 * @param events		Set of events to register at the channel.
	 * 
	 * @return The newly created channel.
	 **/
    public abstract Channel createChannel(Class<? extends Channel> channelClass, String channelName, Set<Class<? extends EventObject>> events);

    /**
	 * Creates and registers a new channel of the supplied channel class, registers 
	 * all events.
	 * 
	 * @param channelClass	The channel implementation to use (default is ChannelImpl).
	 * @param channelName 	Name of the new channel.
	 * @param events		Set of events to register at the channel.
	 * @param listeners		Listeners to register at the channel.
	 * @return The newly created channel.
	 **/
    public abstract ListenerChannel createChannel(Class<? extends ListenerChannel> channelClass, String channelName, Set<Class<? extends EventObject>> events,
            Set<EventListener> listeners);

}