package org.kasource.kaevent.event.config;

import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.EventObject;


/**
 * Holds information on how an event has been configured.
 * 
 * @author rikard
 * @version $Id$
 **/
public interface EventConfig {

    /**
     * Return the listener class associated with the eventClass
     * 
     * @return the listener interface class
     */
    public abstract Class<? extends EventListener> getListener();

    /**
     * Returns the interface method to be invoked, may be null if runtime
     * strategy method for resolve method is used.
     * 
     * @return Returns the interface method to be invoked
     **/
    public Method getEventMethod(EventObject  event);

    /**
     * Return the actual event class
     * 
     * @return the event class
     **/
    public abstract Class<? extends EventObject> getEventClass();

 
    /**
     * Returns the name of the event
     * 
     * @return name of the event
     */
    public String getName();
    

}
