/**
 * 
 */
package org.kasource.kaevent.event.register;

import java.io.IOException;
import java.util.Collection;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.config.EventConfigFactory;
import org.kasource.kaevent.event.export.AnnotationEventExporter;

/**
 * @author rikardwigforss
 *
 */
public class DefaultEventRegister implements EventRegister{
    private static Logger logger = Logger.getLogger(DefaultEventRegister.class);
    private Map<Class<? extends EventObject>, EventConfig> eventsByClass = new HashMap<Class<? extends EventObject>, EventConfig>();
    private Map<Class<? extends EventListener>, EventConfig> eventsByInterface = new HashMap<Class<? extends EventListener>, EventConfig>();
    private AnnotationEventExporter annotationEventExporter;
    private EventConfigFactory eventConfigFactory;
    
    public DefaultEventRegister() throws IOException {
        
    }

    public void initialize() {
        importAnnotatedEvents();
    }
    
    protected void  importAnnotatedEvents() {
        Set<EventConfig> events;
        try {
            events = annotationEventExporter.exportEvents();
            for(EventConfig event : events) {
                registerEvent(event);
            }
        } catch (Exception e) {
          logger.error("Error when importing annotated events",e);
        } 
      
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
    public void registerEvent(EventConfig event) {
        eventsByClass.put(event.getEventClass(), event);
        eventsByInterface.put(event.getListener(), event);
    }

    @Override
    public void registerEvent(Class<? extends EventObject> eventClass) {
            EventConfig eventConfig = eventConfigFactory.createEventConfig(
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
