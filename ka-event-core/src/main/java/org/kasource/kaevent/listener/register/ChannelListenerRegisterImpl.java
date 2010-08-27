/**
 * 
 */
package org.kasource.kaevent.listener.register;

import java.util.Collection;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.kasource.commons.util.ReflectionUtils;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.channel.Channel;
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

    private Map<Class<? extends EventObject>, Map<EventListener,EventListenerRegistration>> listenersByEvent = new HashMap<Class<? extends EventObject>, Map<EventListener,EventListenerRegistration>>();
    private Channel channel;
   
    
    public ChannelListenerRegisterImpl(Channel channel, EventRegister eventRegister, BeanResolver beanResolver) {
        super(eventRegister, beanResolver);
        this.channel = channel;
       
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
    @Override
    public Collection<EventListenerRegistration> getListenersByEvent(EventObject event) {
        Map<EventListener, EventListenerRegistration> listeners =  listenersByEvent.get(event.getClass());
        if(listeners != null) {
            return listeners.values();
        }
        return EMPTY_LISTENER_COLLECTION;
    }

    
    /**
     * Refresh listeners (listenersByEvent). Will find listeners that listens on
     * eventClass but is currently not registered as listener on that class, due
     * to the fact that the listeners has been registered before the eventClass.
     * 
     * @param eventClass
     **/
    @Override
    public void refreshListeners(Class<? extends EventObject> eventClass) {
       EventConfig eventConfig = eventRegister.getEventByClass(eventClass); 
       for(Map<EventListener, EventListenerRegistration> listenerMap : listenersByEvent.values()) {
           for(EventListener listener : listenerMap.keySet()) {
               if(ReflectionUtils.implementsInterface(listener, eventConfig.getListener())) {
                   addListener(listener, eventClass, channel, null);
               }
           }
       }

    }

    protected void filterInterfaces(Set<Class<? extends EventListener>> interfaces) {
        interfaces.retainAll(channel.getSupportedInterfaces());
    }

    
    /**
     * Register a new listener object to this channel
     * 
     * @param listener
     *            Listener  object to register
     * @param filters List<EventFilter<? extends EventObject>> filters
     **/
    public void registerListener(EventListener listener) {
       super.register(listener, channel);
    }
    
    /**
     * Register a new listener object to this channel
     * 
     * @param listener
     *            Listener  object to register
     * @param filters List<EventFilter<? extends EventObject>> filters
     **/
    @Override
    public void registerListener(EventListener listener, List<EventFilter<EventObject>> filters) {
       super.register(listener, channel, filters);
    }

    protected void addListener(EventListener listener, Class<? extends EventObject> eventClass, Object channel, List<EventFilter<EventObject>> filters) {
        Map<EventListener,EventListenerRegistration> listenerMap = listenersByEvent.get(eventClass);
        if(listenerMap == null) {
            listenerMap = new WeakHashMap<EventListener, EventListenerRegistration>();
            listenersByEvent.put(eventClass, listenerMap);
        }
       
        if(filters != null) {
            filters = getApplicableFilters(eventClass, filters);
        }
        listenerMap.put(listener,new EventListenerRegistration(listener, filters));
    }
   
    /**
     * Remove a registered listener from this channel
     * 
     * @param listenersToRemove
     *            Listener(s) object to unregister
     **/
    @Override
    public void unregisterListener(EventListener listener) {
       for(Map<EventListener, EventListenerRegistration> listenerMap : listenersByEvent.values()) {
           if(listenerMap != null){
               listenerMap.remove(listener);
           }
       }

    }
}