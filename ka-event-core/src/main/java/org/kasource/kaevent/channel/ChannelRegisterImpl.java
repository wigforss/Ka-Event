package org.kasource.kaevent.channel;

import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Default implementation of ChannelRegister.
 * 
 * @author Rikard Wigforss
 * @version $Id$
 **/
public class ChannelRegisterImpl implements ChannelRegister {
    private Map<String, Channel> channels = new HashMap<String, Channel>();
    private Map<Class<? extends EventObject>, Set<Channel>> channelsByEvent = 
        new HashMap<Class<? extends EventObject>, Set<Channel>>();
 
    
    
    public ChannelRegisterImpl() {      
    }
    

    /**
     * Add the information that <i>channel</i> now handles event <i>eventClass</i> as well.
     * 
     * @param channel           The channel that has added a new eventClass
     * @param eventClass        The eventClass added to the channel
     **/
    @Override
    public void registerEventHandler(Channel channel, Class<? extends EventObject> eventClass) {
        if (channels.get(channel.getName()) == null) {
            throw new NoSuchChannelException("No Channel named " + channel.getName() + " can be found");
        }
        Set<Channel> channelSet = channelsByEvent.get(eventClass);
        if (channelSet == null) {
            channelSet = new HashSet<Channel>();
            channelsByEvent.put(eventClass, channelSet);
        }
        if (!channelSet.contains(channel)) {
            channelSet.add(channel);
        }
      
    }

    /**
     * Unregister a channel event.
     * 
     * @param channel		Channel to unregister eventClass from.
     * @param eventClass	Class of event to unregister from channel.
     **/
    @Override
    public void unregisterEventHandler(Channel channel, Class<? extends EventObject> eventClass) {
    	Set<Channel> channels = channelsByEvent.get(eventClass);
    	if (channels != null) {
    		channels.remove(channel);
    	}
    }
   
    
    
    /**
     * Returns a channel by name.
     * 
     * @param channelName
     *            Name of channel to return
     * 
     * @return Channel with name <i>channelName</i>
     **/
    @Override
    public Channel getChannel(String channelName) {
        Channel channel = channels.get(channelName);
        if (channel == null) {
            throw new NoSuchChannelException("No Channel named " + channelName + " can be found");
        }
        return channel;
    }

  
    /**
     * Return channel by event class.
     * 
     * @param eventClass
     *            The event class channels should handle
     * 
     * @return All channels that handles the event class <i>eventClass</i>
     **/
    @Override
    public Set<Channel> getChannelsByEvent(Class<? extends EventObject> eventClass) {
        return channelsByEvent.get(eventClass);
    }

   
   

    /**
     * Register a channel.
     * 
     * @param channel
     *            Channel to register
     **/
    @Override
    public void registerChannel(Channel channel) {
        if (!channels.containsKey(channel.getName())) {
            channels.put(channel.getName(), channel);
        }
        
            
        for (Class<? extends EventObject> eventClass : channel.getEvents()) {
            Set<Channel> channelSet = channelsByEvent.get(eventClass);
            if (channelSet == null) {
                channelSet = new HashSet<Channel>();
                channelsByEvent.put(eventClass, channelSet);
            }
            channelSet.add(channel);
        }
        
    }

   
    /**
     * Register a new Event Class at the channel named <i>channelName</i>.
     * 
     * @param channelName       Name of channel to register event class at.
     * @param eventClass        The event class to register.
     * 
     * @throws NoSuchChannelException if no channel can be found named <i>channelName</i>.
     **/
    @Override
    public void registerEvent(String channelName, 
                              Class<? extends EventObject> eventClass) throws NoSuchChannelException {
        Channel channel = channels.get(channelName);
        if (channel != null) {
            channel.registerEvent(eventClass);
        } else {
            throw new NoSuchChannelException("No Channel named " + channelName + " can be found");
        }
        
    }


    /**
     * Returns all channels registered.
     * 
     * @return List of all channels registered.
     **/
    @Override
    public Collection<Channel> getChannels() {    
        return channels.values();
    }
    
   

}
