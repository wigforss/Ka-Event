package org.kasource.kaevent.event.register;

import java.util.Collection;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.config.EventFactory;

/**
 * @author rikardwigforss
 *
 */
public class DefaultEventRegister implements EventRegister {
    private static final Logger LOG = Logger.getLogger(DefaultEventRegister.class);
    
    private Map<Class<? extends EventObject>, EventConfig> eventsByClass = 
    	new HashMap<Class<? extends EventObject>, EventConfig>();
    
    private Map<Class<? extends EventListener>, EventConfig> eventsByInterface = 
    	new HashMap<Class<? extends EventListener>, EventConfig>();
    
    private Map<String, EventConfig> eventsByName = new HashMap<String, EventConfig>();
    
  
    private EventFactory eventFactory;
    
    public DefaultEventRegister() {
    }
    
    public DefaultEventRegister(EventFactory eventFactory) {
        this.eventFactory = eventFactory;
    }
    

    @Override
    public EventConfig getEventByInterface(Class<? extends EventListener> interfaceClass) {
    	EventConfig event =  eventsByInterface.get(interfaceClass);
    	if (event == null) {
    		throw new NoSuchEventException("Can not find any event for interface " + interfaceClass);
    	}
    	return event;
    }
    
    @Override
    public EventConfig getEventByClass(Class<? extends EventObject> eventClass) {
    	EventConfig event =  eventsByClass.get(eventClass);
    	if (event == null) {
    		throw new NoSuchEventException("Can not find any event for event class " + eventClass);
    	}
    	return event;
    }
    
    @Override
    public EventConfig getEventByName(String name) {
    	EventConfig event = eventsByName.get(name);
    	if (event == null) {
    		throw new NoSuchEventException("Can not find any event by name " + name);
    	}
    	return event;
    }

    @Override
    public void registerEvent(EventConfig event) {
        eventsByClass.put(event.getEventClass(), event);
        eventsByInterface.put(event.getListener(), event);
        eventsByName.put(event.getName(), event);
    }
    
    

    @Override
    public void registerEvent(Class<? extends EventObject> eventClass) {
            EventConfig eventConfig = eventFactory.newFromAnnotatedEventClass(
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
