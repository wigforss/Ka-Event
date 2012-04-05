package org.kasource.kaevent.event.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.EventListener;

import org.kasource.kaevent.event.method.MethodResolver;

public interface EventBuilder {

    public EventBuilder name(String name);

    
    /**
     * Bind the interface class to the event with the supplied event method, does not use any annotations.
     * 
     * @param interfaceClass The listener interface to bind to the eventClass.
     * @param eventMethod    The method from the listener interface to use.
     * 
     * @return This builder to allow chaining.
     * 
     * @throws InvalidEventConfigurationException If an interface is already bound via the @Event annotation on the eventClass or via a previous bindInterface call.
     **/
    public EventBuilder bindInterface(Class<? extends EventListener> interfaceClass, Method eventMethod)
                throws InvalidEventConfigurationException;

    /**
     * Bind the interface class to the event with the supplied event resolver, does not use any annotations.
     * 
     * @param interfaceClass    The listener interface to bind to the eventClass.
     * @param methodResolver    The method resolver to use.
     * 
     * @return This builder to allow chaining.
     * @throws InvalidEventConfigurationException If an interface is already bound via the @Event annotation on the eventClass or via a previous bindInterface call.
     **/
    public EventBuilder bindInterface(Class<? extends EventListener> interfaceClass, @SuppressWarnings("rawtypes") MethodResolver methodResolver)
                throws InvalidEventConfigurationException;

    /**
     * Bind the annotation class to the event, note that this annotation needs to have retention RUNTIME
     * for it to be used.
     * 
     * @param annotationClass   Annotation bind to the event.
     * 
     * @return This builder to allow chaining.
     * @throws InvalidEventConfigurationException If an annotation is already bound via the @Event annotation on the eventClass or via a previous bindAnnotation call.
     */
    public EventBuilder bindAnnotation(Class<? extends Annotation> annotationClass)
                throws InvalidEventConfigurationException;

    public EventConfig build();

}
