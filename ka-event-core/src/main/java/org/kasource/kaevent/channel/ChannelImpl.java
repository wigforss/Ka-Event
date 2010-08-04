package org.kasource.kaevent.channel;

import java.util.Collection;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.dispatch.EventMethodInvoker;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.listener.register.ChannelListenerRegister;
import org.kasource.kaevent.listener.register.ChannelListenerRegisterImpl;



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
public class ChannelImpl  implements Channel {
    // Name of the channel
    private String name;
    // Set of event classes this channel handles
    private Map<Class<? extends EventObject>,Class<? extends EventListener>> eventMap = new HashMap<Class<? extends EventObject>,Class<? extends EventListener>>();
    private ChannelListenerRegister listenerRegister;
    private EventRegister eventRegister;
    private ChannelRegister channelRegister;
    private EventMethodInvoker eventMethodInvoker;
    
    
    ChannelImpl(String name, ChannelRegister channelRegister, EventRegister eventRegister, EventMethodInvoker eventMethodInvoker) {
        this.name = name;
        this.channelRegister = channelRegister;
        this.eventRegister = eventRegister;
        this.eventMethodInvoker = eventMethodInvoker;
        listenerRegister = new ChannelListenerRegisterImpl(this, eventRegister);
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
        if (eventConfig == null) {
            throw new IllegalStateException(eventClass + " is not a registered event");
        }
        if (!eventMap.containsKey(eventClass)) { 
                eventMap.put(eventClass, eventConfig.getListener());
                channelRegister.handleEvent(this, eventClass);
        }
    }
    
    

    

    
    /**
     * Fire event synchronously to all channel listeners
     * 
     * @param event     Event to dispatch
     **/
    @Override
    public void fireEvent(EventObject event, boolean blocked) {
        eventMethodInvoker.invokeEventMethod(event, listenerRegister.getListenersByEvent(event), blocked);
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
     * Register a new listener object to this channel
     * 
     * @param listener
     *            Listener object to register
     **/
    /*
    @Override
    public void registerListener(EventListener listener,List<EventFilter<? extends EventObject>> filters) {
        listenerRegister.registerListener(listener);
    }
    */
    /**
     * Register a new listener object to this channel
     * 
     * @param listener
     *            Listener object to register
     **/
    @Override
    public void registerListener(EventListener listener) {
        listenerRegister.registerListener(listener);
    }

    /**
     * Remove a registered listener from this channel
     * 
     * @param listener
     *            Listener object to unregister
     **/
    @Override
    public void unregisterListener(EventListener listener) {
        listenerRegister.unregisterListener(listener);

    }
    
   
   

}
