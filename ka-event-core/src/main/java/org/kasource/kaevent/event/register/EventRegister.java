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
     *    * @throws NoSuchEventException if no event can be found by interfaceClass.
     **/
    public EventConfig getEventByInterface(Class<? extends EventListener> interfaceClass) throws NoSuchEventException;
    
    
    /**
     * Returns true if there's an Event registered to the interfaceCLass 
     * 
     * @param interfaceClass Event Listener Interface class.
     * 
     * @return true if there's an Event registered to the interfaceCLass
     **/
    public boolean hasEventByInterface(Class<? extends EventListener> interfaceClass);

    /**
     * Returns Event Configuration by event class.
     * 
     * @param eventClass Event Class.
     * 
     * @return Event Configuration associated with eventClass.
     * @throws NoSuchEventException if no event can be found of type eventClass.
     **/
    public EventConfig getEventByClass(Class<? extends EventObject> eventClass) throws NoSuchEventException;
    
    
    /**
     * Returns true if there's an Event registered to the eventClass 
     * 
     * @param eventClass Event Class.
     * 
     * @return true if there's an Event registered to the eventClass 
     **/
    public boolean hasEventByClass(Class<? extends EventObject> eventClass);
    
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
