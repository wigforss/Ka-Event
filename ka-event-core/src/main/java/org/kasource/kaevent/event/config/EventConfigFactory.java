/**
 * 
 */
package org.kasource.kaevent.event.config;

import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.EventObject;

import org.kasource.kaevent.event.method.MethodResolver;

/**
 * @author rikardwigforss
 *
 */
public interface EventConfigFactory {

    public abstract EventConfig newFromAnnotatedEventClass(Class<? extends EventObject> event);

    /**
     * Create and an EventConfig based on an annotated event class
     * 
     * @param event
     * @return
     **/
    public abstract EventConfig newFromAnnotatedInterfaceClass(Class<? extends EventObject> event,
            Class<? extends EventListener> listener, String name);

    public abstract EventConfig newWithEventMethod(Class<? extends EventObject> event,
            Class<? extends EventListener> listener, Method eventMethod, String name);

    @SuppressWarnings("unchecked")
    public abstract EventConfig newWithMethodResolver(Class<? extends EventObject> event,
            Class<? extends EventListener> listener, MethodResolver methodResolver, String name);

}