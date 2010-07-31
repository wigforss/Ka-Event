package org.kasource.kaevent.core.channel;

import java.util.EventListener;
import java.util.EventObject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import org.kasource.kaevent.core.event.register.EventRegister;
import org.kasource.kaevent.core.filter.EventFilter;
import org.kasource.kaevent.core.filter.EventFilterRegister;
import org.kasource.kaevent.core.listener.register.ChannelListenerRegister;
import org.springframework.beans.factory.BeanNameAware;



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
    private Set<Class<? extends EventObject>> events = new HashSet<Class<? extends EventObject>>();

    private ChannelListenerRegister listenerRegister;
    private EventRegister eventRegister;
    private ChannelRegister channelRegister;

    ChannelImpl(String name, ChannelRegister channelRegister) {
        this.name = name;
        this.channelRegister = channelRegister;
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
        if (eventRegister.getEventByClass(eventClass) == null) {
            throw new IllegalStateException(eventClass + " is not a registered event");
        }
        if (!events.contains(eventClass)) { 
                events.add(eventClass);
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
        /*if(dispatchHelper.passFilters(event, getEventFilterRegistry().getFilters( event.getClass()))) {
            Set<EventListenerRegistration> channelListeners = getListenerRegistry().getListenersByEventClass(event.getClass());
            if (channelListeners != null) {
                dispatchHelper.callListeners(event, eventRegister.getEventByEventClass(event.getClass()), channelListeners, blocked);
            }
        }*/
    }
    
    /**
     * Returns the set of events this channel handles. 
     * 
     * @return all events this channel handles
     **/
    @Override
    public Set<Class<? extends EventObject>> getEvents() {
        return events;
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
