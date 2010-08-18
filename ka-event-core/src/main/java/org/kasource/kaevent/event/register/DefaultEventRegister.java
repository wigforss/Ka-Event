/**
 * 
 */
package org.kasource.kaevent.event.register;

import java.util.Collection;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.config.EventConfigFactory;

/**
 * @author rikardwigforss
 *
 */
public class DefaultEventRegister implements EventRegister{
    private static Logger logger = Logger.getLogger(DefaultEventRegister.class);
    private Map<Class<? extends EventObject>, EventConfig> eventsByClass = new HashMap<Class<? extends EventObject>, EventConfig>();
    private Map<Class<? extends EventListener>, EventConfig> eventsByInterface = new HashMap<Class<? extends EventListener>, EventConfig>();
    private Map<String, EventConfig> eventsByName = new HashMap<String, EventConfig>();
    private EventConfigFactory eventConfigFactory;
    
    public DefaultEventRegister(EventConfigFactory eventConfigFactory) {
        this.eventConfigFactory = eventConfigFactory;
    }
    
 
    
   
    
   
    
    
    @Override
    public EventConfig getEventByInterface(Class<? extends EventListener> interfaceClass) {
        return eventsByInterface.get(interfaceClass);
    }
    
    @Override
    public EventConfig getEventByClass(Class<? extends EventObject> eventClass) {
        return eventsByClass.get(eventClass);
    }
    
    @Override
    public EventConfig getEventByName(String name) {
        return eventsByName.get(name);
    }

    @Override
    public void registerEvent(EventConfig event) {
        eventsByClass.put(event.getEventClass(), event);
        eventsByInterface.put(event.getListener(), event);
        eventsByName.put(event.getName(), event);
    }
    
    

    @Override
    public void registerEvent(Class<? extends EventObject> eventClass) {
            EventConfig eventConfig = eventConfigFactory.newFromAnnotatedEventClass(
                (Class<? extends EventObject>) eventClass);
            registerEvent(eventConfig);
    }
    
   

  
    @Override
    public Set<Class<? extends EventObject>> getEventClasses() {
        return eventsByClass.keySet();
    }

    
    @Override
    public Collection<EventConfig> getEvents() {
        return eventsByClass.values();
    }

    @Override
    public Set<Class<? extends EventListener>> getListenersInterfaces() {
        return eventsByInterface.keySet();
    }
  
    
    
}
