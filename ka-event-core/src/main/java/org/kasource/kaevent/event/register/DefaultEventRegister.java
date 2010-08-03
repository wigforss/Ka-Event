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
import org.kasource.kaevent.event.export.EventExporter;

/**
 * @author rikardwigforss
 *
 */
public class DefaultEventRegister implements EventRegister{
    private static Logger logger = Logger.getLogger(DefaultEventRegister.class);
    private Map<Class<? extends EventObject>, EventConfig> eventsByClass = new HashMap<Class<? extends EventObject>, EventConfig>();
    private Map<Class<? extends EventListener>, EventConfig> eventsByInterface = new HashMap<Class<? extends EventListener>, EventConfig>();
    private EventExporter eventExporter;
    private EventConfigFactory eventConfigFactory;
    
    public DefaultEventRegister() {
        initialize();
    }
    
    public DefaultEventRegister(EventExporter eventExporter)  {
       this.eventExporter = eventExporter;
       initialize();
    }

    public void initialize() {
       if(eventExporter != null) {
           importEvents();
       }
    }
    
    protected void  importEvents() {
        Set<EventConfig> events;
        try {
            events = eventExporter.exportEvents();
            for(EventConfig event : events) {
                registerEvent(event);
            }
        } catch (RuntimeException re) {
          logger.error("Error when importing annotated events",re);
          throw re;
        } catch (IOException e) {
            throw new IllegalStateException("Could not import events", e);
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
