/**
 * 
 */
package org.kasource.kaevent.listener.register;

import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.kasource.commons.util.ReflectionUtils;
import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.register.EventRegister;

/**
 * A register of all EventListener for a specific channel.
 * 
 * Listeners are held in WeakHashMap to enable garbage collection of listeners no longer referenced outside of 
 * the listenerByEvent map.
 * 
 * @author wigforss
 **/
public class ChannelListenerRegisterImpl extends AbstractEventListenerRegister implements ChannelListenerRegister {

    private Map<Class<? extends EventObject>, Map<EventListener,Object>> listenersByEvent = new HashMap<Class<? extends EventObject>, Map<EventListener,Object>>();
    private Channel channel;
   
    
    public ChannelListenerRegisterImpl(Channel channel, EventRegister eventRegister) {
        super(eventRegister);
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
    public Set<EventListener> getListenersByEvent(EventObject event) {
        Map<EventListener, Object> listeners =  listenersByEvent.get(event.getClass());
        if(listeners != null) {
            return listeners.keySet();
        }
        return EMPTY_LISTENER_SET;
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
       for(Map<EventListener, Object> listenerMap : listenersByEvent.values()) {
           for(EventListener listener : listenerMap.keySet()) {
               if(ReflectionUtils.implementsInterface(listener, eventConfig.getListener())) {
                   addListener(listener, eventClass, channel);
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
       super.registerListener(listener, channel);
    }

    protected void addListener(EventListener listener, Class<? extends EventObject> eventClass, Object channel) {
        Map<EventListener,Object> listenerMap = listenersByEvent.get(eventClass);
        if(listenerMap == null) {
            listenerMap = new WeakHashMap<EventListener, Object>();
            listenersByEvent.put(eventClass, listenerMap);
        }
        listenerMap.put(listener, null);
    }
   
    /**
     * Remove a registered listener from this channel
     * 
     * @param listenersToRemove
     *            Listener(s) object to unregister
     **/
    @Override
    public void unregisterListener(EventListener listener) {
       for(Map<EventListener, Object> listenerMap : listenersByEvent.values()) {
           if(listenerMap != null){
               listenerMap.remove(listener);
           }
       }

    }
}
