package org.kasource.kaevent.event.method;

import java.lang.reflect.Method;
import java.util.EventObject;


/**
 * Finds the method that should be invoked on the registered {@link java.util.EventListener} implementations.
 * 
 * If you want to change how to find which "listener interface" method to
 * trigger. You can implement this interface and reference that implementation in
 * the {@link org.kasource.kaevent.annotations.event.methodresolving.kenai.sadelf.annotations.listener.BeanMethodResolver} or {@link org.kasource.kaevent.annotations.event.methodresolving.kenai.sadelf.annotations.listener.FactoryMethodResolver} 
 * annotation which allows you to use specific a MethodResolver for a specific {@Link java.util.EventListener}
 * implementation.
 * 
 * @author rikard
 * @version $Id$
 **/
public interface MethodResolver<T extends EventObject>{
    /**
     * Returns a valid event interface method to be used. The interface methods
     * must be public, return void and take only one parameter on the eventClass
     * specified in the event information object supplied.
     * 
     * @param event
     *            The event object
     * 
     * @return a valid event interface method to be used.
     * 
     * @throws IllegalStateException
     **/
    public  Method resolveMethod(T event);
}
