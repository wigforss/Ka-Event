package org.kasource.kaevent.channel;

import java.util.Collection;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.register.EventRegister;

public abstract class ChannelAdapter implements Channel {
	
	private Map<Class<? extends EventObject>,Class<? extends EventListener>> eventMap = new HashMap<Class<? extends EventObject>,Class<? extends EventListener>>();
	// Name of the channel
    private String name;
    private ChannelRegister channelRegister;
    private EventRegister eventRegister;
    
    
    public ChannelAdapter(ChannelRegister channelRegister, EventRegister eventRegister) {
    	this.channelRegister = channelRegister;
    	this.eventRegister = eventRegister;
    }

	public ChannelAdapter(String name, ChannelRegister channelRegister, EventRegister eventRegister) {
    	this.name = name;
    	this.channelRegister = channelRegister;
    	this.eventRegister = eventRegister;
    }
    
    /**
     * Return the name of the channel.
     * 
     * @return name of the channel
     **/
    @Override
    public String getName() {
        return name;
    }

    /**
     * Set the name of the channel.
     * 
     * @param name
     *            Name of the channel. Name should be unique within a single
     *            ChannelFactory.
     **/
    @Override
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Register a new Event type to the register.
     * 
     * @param eventClass
     *            New event type to handle
     * @param refreshListeners
     *            true to refresh listeners
     **/
    @Override
    public void registerEvent(Class<? extends EventObject> eventClass) {
        EventConfig eventConfig = eventRegister.getEventByClass(eventClass);
       
        if (!eventMap.containsKey(eventClass)) { 
                eventMap.put(eventClass, eventConfig.getListener());
                channelRegister.registerEventHandler(this, eventClass);
        }
    }
    
    /**
     * Unregister the event from this channel. 
     * 
     * @param eventClass	Class of the event to unregister.
     **/
    @Override
	public void unregisterEvent(Class<? extends EventObject> eventClass) {
		eventMap.remove(eventClass);
		channelRegister.unregisterEventHandler(this, eventClass);
		 
	}
    
    /**
     * Returns the set of events this channel handles. 
     * 
     * @return all events this channel handles
     **/
    @Override
    public Set<Class<? extends EventObject>> getEvents() {
        return eventMap.keySet();
    }
    
    /**
     * Returns the set of events this channel handles. 
     * 
     * @return all events this channel handles
     **/
    @Override
    public Collection<Class<? extends EventListener>> getSupportedInterfaces() {
        
        return eventMap.values();
    }
    
    protected EventRegister getEventRegister() {
		return eventRegister;
	}
}
