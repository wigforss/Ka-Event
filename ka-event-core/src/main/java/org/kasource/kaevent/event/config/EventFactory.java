/**
 * 
 */
package org.kasource.kaevent.event.config;

import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.EventObject;

import org.kasource.kaevent.event.method.MethodResolver;

/**
 * Factory used to create EventConfig with.
 * 
 * @author rikardwigforss
 * @version $Id$
 **/
public interface EventFactory {

	/**
     * Create new event from an @Event annotated class and return it. Event name is set to
     * the event class name.
     * 
     * @param event Event class to create EventConfig for.
     * 
     *  @return a new EventConfig.
     **/
    public abstract EventConfig newFromAnnotatedEventClass(Class<? extends EventObject> event);

    /***
     * Create new event from an @Event annotated class and return it.
     * 
     * @param event Event class to create EventConfig for.
     * @param name Name of the event.
     * 
     * @return a new EventConfig.
     **/
    public abstract EventConfig newFromAnnotatedEventClass(Class<? extends EventObject> event, String name);
    
    /**
     * Create a new event from an event class and an interface class annotated for Method resolving.
     * 
     * If @MethodResolving is missing from the listener, this method tries to find a 
     * event method on the listener class to use automatically.
     * 
     * @param event		Event class to create EventConfig for.
     * @param listener	Event Listener Interface class to create EventConfig for.
     * @param name		Name of the event.
     * @return a new EventConfig.
     **/
    public abstract EventConfig newFromAnnotatedInterfaceClass(Class<? extends EventObject> event,
            Class<? extends EventListener> listener, String name);

    /**
     * Create a new event from an event class, interface class and the event listener method to invoke.
     * 
     * @param event			Event class to create EventConfig for.
     * @param listener		Event Listener Interface class to create EventConfig for.
     * @param eventMethod	Method from listener class to invoke on event.
     * @param name			Name of the event.
     * 
     * @return a new EventConfig.
     **/
    public abstract EventConfig newWithEventMethod(Class<? extends EventObject> event,
            Class<? extends EventListener> listener, Method eventMethod, String name);

    /**
     * Create a new event from an event class, interface class and a MethodResolver.
     * 
     * @param event				Event class to create EventConfig for.
     * @param listener			Event Listener Interface class to create EventConfig for.
     * @param methodResolver	Method Resolver to use.
     * @param name				Name of the event.
     * @return a new EventConfig.
     **/
    public abstract EventConfig newWithMethodResolver(Class<? extends EventObject> event,
            Class<? extends EventListener> listener, MethodResolver<EventObject> methodResolver, String name);

}