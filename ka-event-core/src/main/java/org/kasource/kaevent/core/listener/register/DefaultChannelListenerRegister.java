package org.kasource.kaevent.core.listener.register;



import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.kasource.commons.util.ReflectionUtils;
import org.kasource.kaevent.core.channel.Channel;
import org.kasource.kaevent.core.event.config.EventConfig;
import org.kasource.kaevent.core.event.register.EventRegister;



public class DefaultChannelListenerRegister implements ChannelListenerRegister {
    // Map<EventClass, Set of event listener objects>
    private Map<Class<? extends EventObject>, Map<EventListener, Object>> listenersByEvent = new HashMap<Class<? extends EventObject>, Map<EventListener, Object>>();
    // Set of listeners
    private Map<EventListener, Object> listeners = new WeakHashMap<EventListener, Object>();

    private Channel channel;
    private EventRegister eventRegister;

    public DefaultChannelListenerRegister(Channel channel, EventRegister eventRegister) {
        this.channel = channel;
        this.eventRegister = eventRegister;
    }

    public DefaultChannelListenerRegister(Channel channel, EventRegister eventRegister,
            Set<EventListener> listeners) {
        this.channel = channel;
        this.eventRegister = eventRegister;
        if (listeners != null) {
            for (EventListener listener : listeners) {
                registerListener(listener);
            }
        }
    }

    

    /**
     * Register a new listener object to this channel
     * 
     * @param listener
     *            Listener  object to register
     * @param filters List<EventFilter<? extends EventObject>> filters
     **/
    @SuppressWarnings("unchecked")
    @Override
    public void registerListener(EventListener listener) {
        boolean eventFound = false;
       
        Class[] interfaces = listener.getClass().getInterfaces();
        for (Class interfaceClass : interfaces) {
            if (EventListener.class.isAssignableFrom(interfaceClass)) {
                EventConfig eventConfig = eventRegister.getEventByInterface(interfaceClass);
                if (eventConfig != null) {
                    Class<? extends EventObject> eventClass = eventConfig.getEventClass();
                    if (channel.getEvents().contains(eventClass)) {
                        Map<EventListener, Object> listenerMap = listenersByEvent.get(eventClass);
                        if (listenerMap == null) {
                            listenerMap = new WeakHashMap<EventListener, Object>();
                            listenersByEvent.put(eventClass, listenerMap);
                        }
                        listenerMap.put(listener, null);
                        eventFound = true;
                    }
                }
            }
        }
        if (eventFound) {
            if (!listeners.containsKey(listener)) {
                listeners.put(listener, null);
            }
        } else {
            throw new IllegalStateException("No events found which " + listener
                    + " listens to");
        }
    }

    /**
     * Remove a registered listener from this channel
     * 
     * @param listenersToRemove
     *            Listener(s) object to unregister
     **/
    @SuppressWarnings("unchecked")
    public void unregisterListener(EventListener listener) {

        listeners.remove(listener);
        Class[] interfaces = listener.getClass().getInterfaces();
        for (Class interfaceClass : interfaces) {
            if (EventListener.class.isAssignableFrom(interfaceClass)) {
                EventConfig eventConfig = eventRegister.getEventByInterface(interfaceClass);
                if (eventConfig != null) {
                    Class<? extends EventObject> eventClass = eventConfig.getEventClass();
                    if (channel.getEvents().contains(eventClass)) { // Check
                                                                    // contains
                        Map<EventListener, Object> listenerMap = listenersByEvent.get(eventClass);
                        if (listenerMap != null) {
                            listenerMap.remove(listener);
                        }
                    }
                }
            }
        }

    }

    /**
     * Refresh listeners (listenersByEvent). Will find listeners that listens on
     * eventClass but is currently not registered as listener on that class, due
     * to the fact that the listeners has been registered before the eventClass.
     * 
     * @param eventClass
     **/
    public void refreshListeners(Class<? extends EventObject> eventClass) {
        EventConfig eventConfig = eventRegister.getEventByClass(eventClass);
        for (EventListener listener : listeners.keySet()) {
            if (ReflectionUtils.implementsInterface(listener,eventConfig.getListener())) {
                Map<EventListener, Object> eventListnerMap = listenersByEvent.get(eventClass);
                if (eventListnerMap == null) {
                    eventListnerMap = new WeakHashMap<EventListener, Object>();
                    eventListnerMap.put(listener, null);
                    listenersByEvent.put(eventClass, eventListnerMap);
                } else if (!eventListnerMap.containsKey(listener)) { 
                    eventListnerMap.put(listener, null);
                }
            }
        }
    }

    /**
     * Returns the set of listener objects that listens to the event class
     * <i>eventClass</i>.
     * 
     * @param eventClass
     *            The event class to find listener object for.
     * 
     * @return all listener object of eventClass
     **/
    public Set<EventListener> getListenersByEventClass(Class<? extends EventObject> eventClass) {
        Map<EventListener, Object> listenerMap = listenersByEvent.get(eventClass);
        if (listenerMap == null)
            return null;
        return listenersByEvent.get(eventClass).keySet();
    }

}
