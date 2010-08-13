/**
 * 
 */
package org.kasource.kaevent.event.register;

import java.util.Collection;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Set;

import org.kasource.kaevent.event.config.EventConfig;



/**
 * @author rikardwigforss
 *
 */
public interface EventRegister {

    public EventConfig getEventByInterface(Class<? extends EventListener> interfaceClass);

    public EventConfig getEventByClass(Class<? extends EventObject> eventClass);
    
    public EventConfig getEventByName(String name);

    public void registerEvent(EventConfig event);
    
    public void registerEvent(Class<? extends EventObject> eventClass);
    
    public Set<Class<? extends EventObject>> getEventClasses();
    
    public Set<Class<? extends EventListener>> getListenersInterfaces();
    
    public Collection<EventConfig> getEvents();
}
