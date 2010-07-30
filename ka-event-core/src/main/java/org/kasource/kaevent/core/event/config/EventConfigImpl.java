package org.kasource.kaevent.core.event.config;

import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.EventObject;


import org.kasource.commons.util.ReflectionUtils;
import org.kasource.kaevent.core.event.method.MethodResolver;



/**
 * Holds information on how an event has been configured.
 * 
 * @author rikard
 * @version $Id$
 **/
public class EventConfigImpl implements EventConfig {
    private Class<? extends EventObject> eventClass;
    private Class<? extends EventListener> listener;
    private Method eventMethod; // May be null  
    private MethodResolver<? extends EventObject> methodResolver;
    private String[] channels;

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
    public Method getEventMethod() {
        return eventMethod;
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

 
    /**
     * Returns a method in the listener interface with name <i>methodName</i>.
     * 
     * @param methodName
     *            name of method to return.
     * 
     * @return Method of the method named <i>methodName</i>.
     **/
    @Override
    public Method getListenerMethod(String methodName) {
        return ReflectionUtils.getMethod(listener, methodName, eventClass);
    }

   

    public MethodResolver<? extends EventObject> getMethodResolver() {
        return methodResolver;
    }

    public void setMethodResolver(MethodResolver<? extends EventObject> methodResolver) {
        this.methodResolver = methodResolver;
    }

    @Override
    public String[] getChannels() {
        return channels;
    }

   public void setChannels(String[] channels) {
        this.channels = channels;

    }



/**
 * @param eventMethod the eventMethod to set
 */
public void setEventMethod(Method eventMethod) {
    this.eventMethod = eventMethod;
}

}
