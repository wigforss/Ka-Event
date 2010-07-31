/**
 * 
 */
package org.kasource.kaevent.core.channel;

import java.util.EventObject;
import java.util.Set;

/**
 * @author rikardwigforss
 *
 */
public interface ChannelRegister {
    /**
     * Returns a channel by name
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
     * Return channel by event class
     * 
     * @param eventClass
     *            The event class channels should handle
     * 
     * @return All channels that handles the event class <i>eventClass</i>
     **/
    public abstract Set<Channel> getChannelsByEvent(Class<? extends EventObject> eventClass);

  
          
    
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
    public abstract void registerEvent(String channelName, Class<? extends EventObject> eventClass);
    
    
    /**
     * Add the information that <i>channel</i> now handles event <i>eventClass</i> as well.
     * 
     * NOTE: This method should not only be called by the channel itself. Might be removed from this
     * interface in the future. 
     * 
     * @param channel           The channel that has added a new eventClass
     * @param eventClass        The eventClass added to the channel
     **/
    public void handleEvent(Channel channel, Class<? extends EventObject> eventClass);
  
}
