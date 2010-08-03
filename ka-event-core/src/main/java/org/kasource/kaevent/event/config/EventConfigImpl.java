package org.kasource.kaevent.event.config;

import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.EventObject;

import org.kasource.kaevent.event.method.MethodResolver;



/**
 * Holds information on how an event has been configured.
 * 
 * @author rikard
 * @version $Id$
 **/
public class EventConfigImpl implements EventConfig {
    private Class<? extends EventObject> eventClass;
    private Class<? extends EventListener> listener;
    Method defaultMethod; // May be null  
    MethodResolver methodResolver;
 

    EventConfigImpl(Class<? extends EventObject> eventClass, 
                           Class<? extends EventListener> listener) {
        this.eventClass = eventClass;
        this.listener = listener;   
    }

    

    /**
     * Return the listener class associated with the eventClass
     * 
     * @return the listener interface class
     */
    public Class<? extends EventListener> getListener() {
        return listener;
    }

    

    /**
     * Returns the interface method to be invoked, may be null if runtime
     * strategy method for resolve method is used.
     * 
     * @return Returns the interface method to be invoked
     **/
    @Override
    public  Method getEventMethod(EventObject event) {
        return (defaultMethod != null ? defaultMethod : methodResolver.resolveMethod(event));
    }

    

    /**
     * Return the actual event class
     * 
     * @return the event class
     **/
    @Override
    public Class<? extends EventObject> getEventClass() {
        return eventClass;
    }

 
    
   

   

  




}
