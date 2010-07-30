package org.kasource.kaevent.core.listener.register;

import static com.kenai.sadelf.util.ReflectionUtils.implementsInterface;

import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import com.kenai.sadelf.event.config.EventConfig;
import com.kenai.sadelf.event.config.EventConfigFactory;
import com.kenai.sadelf.event.filter.EventFilter;
import com.kenai.sadelf.event.register.EventListenerRegistration;

public class DefaultChannelListenerRegister implements ChannelListenerRegister {
    // Map<EventClass, Set of event listener objects>
    private Map<Class<? extends EventObject>, Map<EventListenerRegistration, Object>> listenersByEvent = new HashMap<Class<? extends EventObject>, Map<EventListenerRegistration, Object>>();
    // Set of listeners
    private Map<EventListener, Object> listeners = new WeakHashMap<EventListener, Object>();

    private Channel channel;
    private EventConfigRegister eventConfigFactory;

    public DefaultChannelListenerRegister(Channel channel, EventConfigRegister eventConfigFactory) {
        this.channel = channel;
        this.eventConfigFactory = eventConfigFactory;
    }

    public DefaultChannelListenerRegister(Channel channel, EventConfigRegister eventConfigFactory,
            Set<EventListener> listeners) {
        this.channel = channel;
        this.eventConfigFactory = eventConfigFactory;
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
     **/
    @Override
    public void registerListener(EventListener listener) {
        registerListener(listener, null);
    }

    /**
     * Register a new listener object to this channel
     * 
     * @param listener
     *            Listener  object to register
     * @param filters
     **/
    @SuppressWarnings("unchecked")
    @Override
    public void registerListener(EventListener listener, List<EventFilter<? extends EventObject>> filters) {
        boolean eventFound = false;
        EventListenerRegistration listenerRegistration = null;
        if (filters == null) {
            listenerRegistration = new EventListenerRegistration(listener, eventConfigFactory);
        } else {
            listenerRegistration = new EventListenerRegistration(listener, eventConfigFactory, filters);
        }
        Class[] interfaces = listenerRegistration.getEventListener().getClass().getInterfaces();
        for (Class interfaceClass : interfaces) {
            if (EventListener.class.isAssignableFrom(interfaceClass)) {
                EventConfig eventConfig = eventConfigFactory.getEventByListener(interfaceClass);
                if (eventConfig != null) {
                    Class<? extends EventObject> eventClass = eventConfig.getEventClass();
                    if (channel.getEvents().contains(eventClass)) {
                        Map<EventListenerRegistration, Object> listenerMap = listenersByEvent.get(eventClass);
                        if (listenerMap == null) {
                            listenerMap = new WeakHashMap<EventListenerRegistration, Object>();
                            listenersByEvent.put(eventClass, listenerMap);
                        }
                        listenerMap.put(listenerRegistration, null);
                        eventFound = true;
                    }
                }
            }
        }
        if (eventFound) {
            if (!listeners.containsKey(listenerRegistration.getEventListener())) {
                listeners.put(listenerRegistration.getEventListener(), null);
            }
        } else {
            throw new IllegalStateException("No events found which " + listenerRegistration.getEventListener()
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
                EventConfig eventConfig = eventConfigFactory.getEventByListener(interfaceClass);
                if (eventConfig != null) {
                    Class<? extends EventObject> eventClass = eventConfig.getEventClass();
                    if (channel.getEvents().contains(eventClass)) { // Check
                                                                    // contains
                        Map<EventListenerRegistration, Object> listenerMap = listenersByEvent.get(eventClass);
                        if (listenerMap != null) {
                            listenerMap.remove(new EventListenerRegistration(listener, eventConfigFactory));
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
        EventConfig eventConfig = eventConfigFactory.getEventByEventClass(eventClass);
        for (EventListener listener : listeners.keySet()) {
            if (implementsInterface(eventConfig.getListener(), listener)) {
                Map<EventListenerRegistration, Object> eventListnerMap = listenersByEvent.get(eventClass);
                if (eventListnerMap == null) {
                    eventListnerMap = new WeakHashMap<EventListenerRegistration, Object>();
                    eventListnerMap.put(new EventListenerRegistration(listener, eventConfigFactory), null);
                    listenersByEvent.put(eventClass, eventListnerMap);
                } else if (!eventListnerMap.containsKey(listener)) { 
                    eventListnerMap.put(new EventListenerRegistration(listener, eventConfigFactory), null);
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
    public Set<EventListenerRegistration> getListenersByEventClass(Class<? extends EventObject> eventClass) {
        Map<EventListenerRegistration, Object> listenerMap = listenersByEvent.get(eventClass);
        if (listenerMap == null)
            return null;
        return listenersByEvent.get(eventClass).keySet();
    }

}
