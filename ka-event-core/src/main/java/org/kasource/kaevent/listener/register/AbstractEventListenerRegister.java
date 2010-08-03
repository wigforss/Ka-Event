/**
 * 
 */
package org.kasource.kaevent.listener.register;

import java.util.EventListener;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Set;

import org.kasource.commons.util.ReflectionUtils;
import org.kasource.kaevent.event.register.EventRegister;

/**
 * @author rikardwigforss
 *
 */
public abstract class AbstractEventListenerRegister implements EventListenerRegister{
    protected static final Set<EventListener> EMPTY_LISTENER_SET = new HashSet<EventListener>();
    
    protected EventRegister eventRegister;
    
    public AbstractEventListenerRegister(EventRegister eventRegister) {
        this.eventRegister = eventRegister;
    }
    
    protected void filterInterfaces(Set<Class<? extends EventListener>> interfaces) {}
    
    
    @SuppressWarnings("unchecked")
    private Set<Class<? extends EventListener>> getRegisteredInterfaces(EventListener listener) {
        Set<Class<?>> interfaces = ReflectionUtils.getInterfacesExtending(listener, EventListener.class);
        Set<Class<? extends EventListener>> registeredEvents = new HashSet<Class<? extends EventListener>>();
        for(Class<?> interfaceClass : interfaces) {
            if(eventRegister.getEventByInterface((Class<? extends EventListener>) interfaceClass) != null) {
                registeredEvents.add((Class<? extends EventListener>) interfaceClass);
            }
        }
        filterInterfaces(registeredEvents);
        return registeredEvents;
    }
     
    protected abstract void addListener(EventListener listener, Class<? extends EventObject> eventClass, Object sourceObject);
    
  
    public void registerListener(EventListener listener, Object sourceObject) {
        Set<Class<? extends EventListener>> interfaces = getRegisteredInterfaces(listener);
        for(Class<? extends EventListener> interfaceClass : interfaces) {
            addListener(listener, eventRegister.getEventByInterface(interfaceClass).getEventClass(), sourceObject);
        }
       
    }
}
