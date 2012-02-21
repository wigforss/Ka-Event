package org.kasource.kaevent.listener.register;

import java.util.Collection;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.kasource.commons.reflection.ReflectionUtils;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.channel.ListenerChannel;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.filter.EventFilter;
import org.kasource.kaevent.event.register.EventRegister;

/**
 * A register of all EventListener for a specific channel.
 * 
 * Listeners are held in WeakHashMap to enable garbage collection of listeners no longer referenced outside of 
 * the listenerByEvent map.
 * 
 * @author Rikard Wigforss
 **/
public class ChannelListenerRegisterImpl extends AbstractEventListenerRegister implements ChannelListenerRegister {

    private Map<Class<? extends EventObject>, Map<Object, EventListenerRegistration>> listenersByEvent = 
    	new HashMap<Class<? extends EventObject>, Map<Object, EventListenerRegistration>>();
    
    private ListenerChannel channel;
   
   /***
    * Constructor.
    *  
    * @param channel        Channel.
    * @param eventRegister  Event Register.
    * @param beanResolver   Bean Resolver.
    **/
    public ChannelListenerRegisterImpl(ListenerChannel channel, 
    								   EventRegister eventRegister, 
    								   BeanResolver beanResolver) {
        super(eventRegister, beanResolver);
        this.channel = channel;
       
    }
    
    /**
     * Returns the set of listener objects that listens to the event class
     * <i>eventClass</i>.
     * 
     * @param event
     *            The event object to find listener object for.
     * 
     * @return all listener object of eventClass
     **/
    @Override
    public Collection<EventListenerRegistration> getListenersByEvent(EventObject event) {
    	return getListenersByEvent(event.getClass());
    }
    
    
    /**
     * Returns all event listener registration by an event.
     * 
     * @param eventClass Event to get listeners for.
     * 
     * @return All Listeners for an event.
     **/
    @Override
    public Collection<EventListenerRegistration> getListenersByEvent(Class<? extends EventObject> eventClass) {
        Map<Object, EventListenerRegistration> listeners =  listenersByEvent.get(eventClass);
        if (listeners != null) {
            return listeners.values();
        }
        return EMPTY_LISTENER_COLLECTION;
    }

    
    /**
     * Refresh listeners (listenersByEvent). Will find listeners that listens on
     * eventClass but is currently not registered as listener on that class, due
     * to the fact that the listeners has been registered before the eventClass.
     * 
     * @param eventClass Event class.
     **/
    @Override
    public void refreshListeners(Class<? extends EventObject> eventClass) {
       EventConfig eventConfig = getEventRegister().getEventByClass(eventClass); 
       for (Map<Object, EventListenerRegistration> listenerMap : listenersByEvent.values()) {
           for (Object listener : listenerMap.keySet()) {
               if (ReflectionUtils.implementsInterface(listener, eventConfig.getListener())) {
                   addListener(listener, eventClass, channel, null);
               }
           }
       }

    }

    /**
     * Removes interfaces not supported by the channel.
     * 
     * @param interfaces interface set to remove unsupported interfaces from.
     **/
    protected void filterInterfaces(Set<Class<? extends EventListener>> interfaces) {
        interfaces.retainAll(channel.getSupportedInterfaces());
    }

    
    /**
     * Register a new listener object to this channel.
     * 
     * @param listener
     *            Listener  object to register
     **/
    public void registerListener(Object listener) {
       super.register(listener, channel);
    }
    
    /**
     * Register a new listener object to this channel.
     * 
     * @param listener
     *            Listener  object to register
     * @param filters List<EventFilter<? extends EventObject>> filters
     **/
    @Override
    public void registerListener(Object listener, List<EventFilter<EventObject>> filters) {
       super.register(listener, channel, filters);
    }

    /**
     * Add listener.
     * 
     * @param listener         Listener to add.
     * @param eventClass       Event class.
     * @param dummy            Object not used.
     * @param filters          Filters for listener
     **/
    protected void addListener(Object listener, 
    						   Class<? extends EventObject> eventClass, 
    						   Object dummy,
    						   List<EventFilter<EventObject>> filters) {
        Map<Object, EventListenerRegistration> listenerMap = listenersByEvent.get(eventClass);
        if (listenerMap == null) {
            listenerMap = new WeakHashMap<Object, EventListenerRegistration>();
            listenersByEvent.put(eventClass, listenerMap);
        }
       
        if (filters != null) {
            filters = getApplicableFilters(eventClass, filters);
        }
        listenerMap.put(listener, new EventListenerRegistration(listener, filters));
    }
   
    /**
     * Remove a registered listener from this channel.
     * 
     * @param listener
     *            Listener object to unregister
     **/
    @Override
    public void unregisterListener(Object listener) {
       for (Map<Object, EventListenerRegistration> listenerMap : listenersByEvent.values()) {
           if (listenerMap != null) {
               listenerMap.remove(listener);
           }
       }

    }
}
