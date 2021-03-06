package org.kasource.kaevent.event.register;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.kasource.kaevent.annotations.event.Event;
import org.kasource.kaevent.event.config.EventBuilderFactory;
import org.kasource.kaevent.event.config.EventConfig;

/**
 * @author rikardwigforss
 *
 */
public class DefaultEventRegister implements EventRegister {
   // private static final Logger LOG = Logger.getLogger(DefaultEventRegister.class);
    
    private Map<Class<? extends EventObject>, EventConfig> eventsByClass = 
    	new ConcurrentHashMap<Class<? extends EventObject>, EventConfig>();
    
    private Map<Class<? extends EventListener>, EventConfig> eventsByInterface = 
    	new ConcurrentHashMap<Class<? extends EventListener>, EventConfig>();
    
    private Map<Class<? extends Annotation>, EventConfig> eventsByAnnotation = 
        new ConcurrentHashMap<Class<? extends Annotation>, EventConfig>();
    
    private Map<String, EventConfig> eventsByName = new ConcurrentHashMap<String, EventConfig>();
    
  
    private EventBuilderFactory eventBuilderFactory;
    
    
    /**
     * Constructor.
     * 
     * @param eventBuilderFactory Event Factory to use.
     **/
    public DefaultEventRegister(EventBuilderFactory eventBuilderFactory) {
        this.eventBuilderFactory = eventBuilderFactory;
    }
    

    @Override
    public EventConfig getEventByInterface(Class<? extends EventListener> interfaceClass) {
    	EventConfig event =  eventsByInterface.get(interfaceClass);
    	if (event == null) {
    		throw new NoSuchEventException("Can not find any event for interface " + interfaceClass);
    	}
    	return event;
    }
    
    
    /**
     * Returns true if there's an Event registered to the interfaceCLass 
     * 
     * @param interfaceClass Event Listener Interface class.
     * 
     * @return true if there's an Event registered to the interfaceCLass
     **/
    public boolean hasEventByInterface(Class<? extends EventListener> interfaceClass) {
        return eventsByInterface.containsKey(interfaceClass);
    }
    
    /**
     * Returns Event Configuration by event class.
     * 
     * @param eventClass Event Class.
     * 
     * @return Event Configuration associated with eventClass.
     * @throws NoSuchEventException if no event can be found of type eventClass.
     **/
    @Override
    public EventConfig getEventByClass(Class<? extends EventObject> eventClass) throws NoSuchEventException{
    	EventConfig event =  eventsByClass.get(eventClass);
    	if (event == null) {
    		throw new NoSuchEventException("Can not find any event for event class " + eventClass);
    	}
    	return event;
    }
    
    
    /**
     * Returns true if there's an Event registered to the eventClass 
     * 
     * @param eventClass Event Class.
     * 
     * @return true if there's an Event registered to the eventClass 
     **/
    public boolean hasEventByClass(Class<? extends EventObject> eventClass) {
        return eventsByClass.containsKey(eventClass);
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
        if (event.getListener() != null && event.getListener() != EventListener.class) {
            eventsByInterface.put(event.getListener(), event);
        } 
        if(event.getEventAnnotation() != null && event.getEventAnnotation() != Event.class) {
            eventsByAnnotation.put(event.getEventAnnotation(), event);
        }
        eventsByName.put(event.getName(), event);
    }
    
    

    public Set<Class<? extends Annotation>> getRegisteredEventAnnotations() {
        return eventsByAnnotation.keySet();
    }
    
    public EventConfig getEventByAnnotation(Class<? extends Annotation> eventAnnotation) {
        EventConfig event = eventsByAnnotation.get(eventAnnotation);
        if (event == null) {
            throw new NoSuchEventException("Can not find any event by annotation " + eventAnnotation);
        }
        return event;
    }
    
    @Override
    public void registerEvent(Class<? extends EventObject> eventClass) {
            EventConfig eventConfig = eventBuilderFactory.getBuilder(eventClass).build();
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
