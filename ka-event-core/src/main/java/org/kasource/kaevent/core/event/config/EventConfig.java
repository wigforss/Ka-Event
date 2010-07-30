package org.kasource.kaevent.core.event.config;

import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.EventObject;

import org.kasource.kaevent.core.event.method.MethodResolver;


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
    public abstract Method getEventMethod();

    /**
     * Return the actual event class
     * 
     * @return the event class
     **/
    public abstract Class<? extends EventObject> getEventClass();

 
    /**
     * Returns a method in the listener interface with name <i>methodName</i>.
     * 
     * @param methodName name of method to return.
     * 
     * @return Method of the method named <i>methodName</i>.
     **/
    public  Method getListenerMethod(String methodName);


    public abstract MethodResolver<? extends EventObject> getMethodResolver();
    
    public String[] getChannels();
    
   

}