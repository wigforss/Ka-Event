/**
 * 
 */
package org.kasource.kaevent.channel;

import java.util.Collection;
import java.util.EventObject;
import java.util.Set;

/**
 * Register of all Channels.
 * 
 * Manages and register channels.
 * 
 * @author rikardwigforss
 * @version $Id$
 **/
public interface ChannelRegister {
    
    /**
     * Returns a channel by name.
     * 
     * @param channelName
     *            Name of channel to return
     * 
     * @return Channel with name <i>channelName</i>
     * 
     * @throws NoSuchChannelException if no channel can be found named <i>channelName</i>.
     **/
    public abstract Channel getChannel(String channelName) throws NoSuchChannelException;

    /**
     * Return channel by event class.
     * 
     * @param eventClass
     *            The event class channels should handle
     * 
     * @return All channels that handles the event class <i>eventClass</i>
     **/
    public abstract Set<Channel> getChannelsByEvent(Class<? extends EventObject> eventClass);

    /**
     * Returns all channels registered.
     * 
     * @return List of all channels registered.
     **/
    public abstract Collection<Channel> getChannels();      
    
    /**
     * Register a channel with this factory.
     * 
     * @param channel
     *            Channel to register
     **/
    public abstract void registerChannel(Channel channel);

    /**
     * Register a new Event Class at the channel named <i>channelName</i>.
     * 
     * @param channelName       Name of channel to register event class at.
     * @param eventClass        The event class to register.
     * 
     * @throws NoSuchChannelException if no channel can be found named <i>channelName</i>.
     **/
    public abstract void registerEvent(String channelName, 
                                       Class<? extends EventObject> eventClass) throws NoSuchChannelException;
    
    
    /**
     * Add the information that <i>channel</i> now handles event <i>eventClass</i> as well.
     * 
     * NOTE: This method should not only be called by the channel itself. Might be removed from this
     * interface in the future. 
     * 
     * @param channel           The channel that has added a new eventClass
     * @param eventClass        The eventClass added to the channel
     **/
    public void registerEventHandler(Channel channel, Class<? extends EventObject> eventClass);
    
    /**
     * Unregister a channel event.
     * 
     * @param channel		Channel to unregister eventClass from.
     * @param eventClass	Class of event to unregister from channel.
     **/
    public void unregisterEventHandler(Channel channel, Class<? extends EventObject> eventClass);
    
  
}
