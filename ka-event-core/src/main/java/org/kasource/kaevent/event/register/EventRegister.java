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
 * Event Register.
 * 
 * Register of all Events.
 * 
 * @author rikardwigforss
 */
public interface EventRegister {

    /**
     * Returns Event Configuration by interface.
     * 
     * @param interfaceClass Event Listener Interface class.
     * 
     * @return Event Configuration associated with interfaceCLass.
     **/
    public EventConfig getEventByInterface(Class<? extends EventListener> interfaceClass);

    /**
     * Returns Event Configuration by event class.
     * 
     * @param eventClass Event Class.
     * 
     * @return Event Configuration associated with eventClass.
     **/
    public EventConfig getEventByClass(Class<? extends EventObject> eventClass);
    
    /**
     * Returns Event Configuration by name.
     * 
     * @param name Name of the event.
     * 
     * @return Event Configuration associated with name.
     **/
    public EventConfig getEventByName(String name);

    /**
     * Register an Event Configuration.
     * 
     * @param event Event to register.
     **/
    public void registerEvent(EventConfig event);
    
    /**
     * Register an Event Class annotated with @Event.
     * 
     * @param eventClass Event Class to register.
     **/
    public void registerEvent(Class<? extends EventObject> eventClass);
    
    /**
     * Returns all Event Classes registered.
     * 
     * @return all Event Classes registered.
     **/
    public Set<Class<? extends EventObject>> getEventClasses();
    
    /**
     * Returns all Event Listener Interfaces registered.
     * 
     * @return all Event Listener Interfaces registered.
     **/
    public Set<Class<? extends EventListener>> getListenersInterfaces();
    
    /**
     * Returns all Event Configurations Registered.
     * 
     * @return all Event Configurations Registered.
     **/
    public Collection<EventConfig> getEvents();
}
