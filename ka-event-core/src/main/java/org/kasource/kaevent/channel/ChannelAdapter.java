package org.kasource.kaevent.channel;

import java.util.Collection;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.kasource.kaevent.event.ForwardedEvent;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.event.register.NoSuchEventException;

/**
 * Channel Adapter.
 * 
 * Extend this class to inherit event registration capabilities.
 * 
 * @author rikardwi
 **/
public abstract class ChannelAdapter implements Channel {
	
	private Map<Class<? extends EventObject>, Class<? extends EventListener>> eventMap = 
	    new HashMap<Class<? extends EventObject>, Class<? extends EventListener>>();
	
	/** Name of the channel. **/
    private String name;
    private ChannelRegister channelRegister;
    private EventRegister eventRegister;
    
    /**
     * Constructor.
     * 
     * @param channelRegister Channel register
     * @param eventRegister   Event Register.
     **/
    public ChannelAdapter(ChannelRegister channelRegister, EventRegister eventRegister) {
    	this.channelRegister = channelRegister;
    	this.eventRegister = eventRegister;
    }

    /**
     * Constructor.
     * 
     * @param name            Name of the channel.
     * @param channelRegister Channel register
     * @param eventRegister   Event Register.
     **/
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
     *            
     * @throws NoSuchEventException if no event can be found of type eventClass.
     **/
    @Override
    public void registerEvent(Class<? extends EventObject> eventClass) throws NoSuchEventException {
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
    
    /**
     * Returns the event register.
     * 
     * @return event register instance.
     **/
    protected EventRegister getEventRegister() {
		return eventRegister;
	}
    
    /**
     * Does this channel accept the forwarded event, from another other event system.
     * 
     * For Channels that bridge events to another event solution, which 
     * ka-event might listen to, those forwarded events should not be accepted
     * in order to prevent an infinite loop
     * 
     * @return true to accept the forwarded event, else false.
     **/
    public boolean acceptForwardedEvent(ForwardedEvent event) {
        return true;
    }
}
