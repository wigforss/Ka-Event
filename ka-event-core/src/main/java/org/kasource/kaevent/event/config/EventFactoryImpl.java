package org.kasource.kaevent.event.config;

import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Set;

import org.kasource.commons.reflection.ReflectionUtils;
import org.kasource.kaevent.annotations.event.Event;
import org.kasource.kaevent.annotations.event.methodresolving.MethodResolving;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.event.method.MethodResolver;

/**
 * Default implementation of EventFactory, used to create EventConfig objects.
 * 
 * An EventConfig object must have either an defaultMethod or a methodResolver
 * 
 * @author Rikard Wigforss
 * @version $Id$
 */
public class EventFactoryImpl implements EventFactory {
  
	private AnnotationMethodResolverExtractor methodResolverExtractor;
	
    protected EventFactoryImpl() {    
    }
    
    /**
     * Constructor.
     * 
     * @param beanResolver Bean resolver to use.
     **/
    public EventFactoryImpl(BeanResolver beanResolver) {
        methodResolverExtractor = new AnnotationMethodResolverExtractor(beanResolver);
    }

    /**
     * Create new event from an @Event annotated class and return it. Event name is set to
     * the event class name.
     * 
     * @param event Event class to create EventConfig for.
     * 
     * @return a new EventConfig.
     **/
    public EventConfig newFromAnnotatedEventClass(Class<? extends EventObject> event) {
       return  newFromAnnotatedEventClass(event, event.getName());
    }
    
    /**
     * Create new event from an @Event annotated class and return it.
     * 
     * @param event Event class to create EventConfig for.
     * @param name Name of the event.
     * 
     * @return a new EventConfig.
     **/
    public EventConfig newFromAnnotatedEventClass(Class<? extends EventObject> event, String name) {
        Event eventAnnotation = event.getAnnotation(Event.class);
        if (eventAnnotation == null) {
            throw new IllegalArgumentException(event + " is not annotated with @Event!");
        }
       return  newFromAnnotatedInterfaceClass(event, event.getAnnotation(Event.class).listener(), name);
    }
    
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
    public EventConfig newFromAnnotatedInterfaceClass(Class<? extends EventObject> event, 
    												  Class<? extends EventListener> listener, 
    												  String name) {
        EventConfigImpl eventConfig = new EventConfigImpl(event, listener, name);
        MethodResolving methodResolving = listener.getAnnotation(MethodResolving.class);
        if (methodResolving != null) {
            eventConfig.methodResolver = methodResolverExtractor.getMethodResolver(event, listener, methodResolving);
        } else {
                setDefaultMethod(eventConfig, event, listener);
        }
        return eventConfig;
    }

    /**
     * Find the default method to invoke on listener and sets it on
     * the eventConfig.
     * 
     * @param eventConfig	The EventConfig.
     * @param event			Event class	
     * @param listener		Event Listener Interface class
     * 
     **/
    private void setDefaultMethod(EventConfigImpl eventConfig, Class<? extends EventObject> event,
            Class<? extends EventListener> listener) {
        if (ReflectionUtils.getDeclaredMethodCount(listener) == 1) {
            Set<Method> methodSet = ReflectionUtils.getDeclaredMethodsMatchingReturnType(listener, Void.TYPE, event);
            eventConfig.defaultMethod = methodSet.iterator().next();
        } else {
        	throw new IllegalStateException("EventListener " + listener 
        			+ " should only have one method declared if not annotated with @MethodResolving"); 
        }
    }

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
    public EventConfig newWithEventMethod(Class<? extends EventObject> event, Class<? extends EventListener> listener,
            Method eventMethod, String name) {
    	EventConfigImpl eventConfig = new EventConfigImpl(event, listener, name);
        ReflectionUtils.verifyMethodSignature(eventMethod, Void.TYPE, event);
        eventConfig.defaultMethod = eventMethod;
        return eventConfig;
    }

    /**
     * Create a new event from an event class, interface class and a MethodResolver.
     * 
     * @param event				Event class to create EventConfig for.
     * @param listener			Event Listener Interface class to create EventConfig for.
     * @param methodResolver	Method Resolver to use.
     * @param name				Name of the event.
     * @return a new EventConfig.
     **/
    public EventConfig newWithMethodResolver(Class<? extends EventObject> event, Class<? extends EventListener> listener,
            MethodResolver<EventObject> methodResolver, String name) {
    	EventConfigImpl eventConfig = new EventConfigImpl(event, listener, name);
        if(methodResolver == null) {
        	throw new IllegalArgumentException("methodResolver is not allowed to be null");
        }
        eventConfig.methodResolver = methodResolver;
        return eventConfig;
    }

}
