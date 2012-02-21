package org.kasource.kaevent.event.config;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Set;

import org.kasource.commons.reflection.ReflectionUtils;
import org.kasource.kaevent.annotations.event.Event;
import org.kasource.kaevent.annotations.event.methodresolving.MethodResolving;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.event.method.AnnotatedMethodMethodResolver;
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

    /**
     * Constructor.
     */
    protected EventFactoryImpl() {
    }

    /**
     * Constructor.
     * 
     * @param beanResolver
     *            Bean resolver to use.
     **/
    public EventFactoryImpl(BeanResolver beanResolver) {
        methodResolverExtractor = new AnnotationMethodResolverExtractor(beanResolver);
    }

    /**
     * Create new event from an @Event annotated class and return it. Event name is set to the event class name.
     * 
     * @param event
     *            Event class to create EventConfig for.
     * 
     * @return a new EventConfig.
     **/
    public EventConfig newFromAnnotatedEventClass(Class<? extends EventObject> event) {
        return newFromAnnotatedEventClass(event, event.getName());
    }

    /**
     * Create new event from an @Event annotated class and return it.
     * 
     * @param event
     *            Event class to create EventConfig for.
     * @param name
     *            Name of the event.
     * 
     * @return a new EventConfig.
     **/
    public EventConfig newFromAnnotatedEventClass(Class<? extends EventObject> event, String name) {
        Event eventAnnotation = event.getAnnotation(Event.class);
        if (eventAnnotation == null) {
            throw new IllegalArgumentException(event + " is not annotated with @Event!");
        }
        return newFromAnnotatedInterfaceAndOrAnnotationClass(eventAnnotation, event, name);
    }

    /**
     * Create a new event from an event class and an interface class annotated for Method resolving.
     * 
     * If @MethodResolving is missing from the listener, this method tries to find a event method on the listener class
     * to use automatically.
     * 
     * @param event
     *            Event class to create EventConfig for.
     * @param listener
     *            Event Listener Interface class to create EventConfig for.
     * @param name
     *            Name of the event.
     * @return a new EventConfig.
     **/
    public EventConfig newFromAnnotatedInterfaceAndOrAnnotationClass(Event eventAnnotation, Class<? extends EventObject> event, String name) {
        if (eventAnnotation.listener() != EventListener.class && eventAnnotation.annotation() != Event.class) {
            return newFromInterfaceAndMethodAnnotation(event, eventAnnotation.listener(), eventAnnotation.annotation(), name);
        } else if (eventAnnotation.listener() != EventListener.class) {
            return newFromAnnotatedInterfaceClass(event, eventAnnotation.listener(), name);
        } else if (eventAnnotation.annotation() != Event.class) {
            return newFromMethodAnnotation(event, eventAnnotation.annotation(), name);
        } else {
            throw new IllegalArgumentException("@Event of " + event + " must have either the listener or the annotation attribute set");
        }
        
    }

    /**
     * Create a new event from an event class and an annotation class.
     * 
     * 
     * @param event            Event class to create EventConfig for.
     * @param eventAnnotation  Event Method Annotation to create EventConfig for.
     * @param name             Name of the event.
     * @return a new EventConfig.
     **/
    public EventConfig newFromMethodAnnotation(Class<? extends EventObject> event,
                Class<? extends Annotation> targetAnnotation, String name) {
        validateTargetAnnotation(targetAnnotation);
        AnnotatedMethodMethodResolver methodResolver = new AnnotatedMethodMethodResolver(targetAnnotation, null);
        EventConfigImpl eventConfig = new EventConfigImpl(event, methodResolver, name);
        
        return eventConfig;
    }

    

    
    /**
     * Validates that the target annotation is a valid annotation for a event method annotation.
     * 
     * @param targetAnnotation
     * @throws IllegalStateException if target annotation does not have Retention.RUNTIME and Target does not include ElementType.METHOD.
     **/
    private void validateTargetAnnotation(Class<? extends Annotation> targetAnnotation) throws IllegalStateException{
        Retention annotationRetention = targetAnnotation.getAnnotation(Retention.class);
        if(annotationRetention == null || annotationRetention.value() != RetentionPolicy.RUNTIME) {
            throw new IllegalStateException(targetAnnotation + " must have use retention policy RUNTIME to be used as an Event Method Annotation");
        }
        Target annotationTarget = targetAnnotation.getAnnotation(Target.class);
        boolean methodTarget = false;
        if(annotationTarget != null) {
            ElementType[] elemntTypes = annotationTarget.value();
            for (ElementType type : elemntTypes) {
                if(type == ElementType.METHOD) {
                    methodTarget = true;
                    break;
                }
            }
        }
        if(!methodTarget) {
            throw new IllegalStateException("It must be possible to annotate a methd with " + targetAnnotation + " in order to use it as a Event Method Annotation, ensure that the @Target annotation has ElementType.METHOD set.");
        }
    }
    
    /**
     * Create a new event from an event class and an interface class annotated for Method resolving.
     * 
     * If @MethodResolving is missing from the listener, this method tries to find a event method on the listener class
     * to use automatically.
     * 
     * @param event
     *            Event class to create EventConfig for.
     * @param listener
     *            Event Listener Interface class to create EventConfig for.
     * @param name
     *            Name of the event.
     * @return a new EventConfig.
     **/
    public EventConfig newFromAnnotatedInterfaceClass(Class<? extends EventObject> event,
                Class<? extends EventListener> listener, String name) {
        EventConfigImpl eventConfig = new EventConfigImpl(event, listener, name);
        MethodResolving methodResolving = listener.getAnnotation(MethodResolving.class);
        if (methodResolving != null) {
            eventConfig.setMethodResolver(methodResolverExtractor.getMethodResolver(event, listener, methodResolving));
        } else {
            setDefaultMethod(eventConfig, event, listener);
        }
        return eventConfig;
    }

    /**
     * Create a new event from an event class, an interface class and an annotation class.
     * 
     * 
     * @param event            Event class to create EventConfig for.
     * @param listener Event Listener Interface class to create EventConfig for.
     * @param eventAnnotation  Event Method Annotation to create EventConfig for.
     * @param name             Name of the event.
     * @return a new EventConfig.
     **/
    public EventConfig newFromInterfaceAndMethodAnnotation(Class<? extends EventObject> event,
                Class<? extends EventListener> listener, Class<? extends Annotation> eventAnnotation, String name) {
        EventConfigImpl eventConfig = new EventConfigImpl(event, listener, name);
        MethodResolving methodResolving = listener.getAnnotation(MethodResolving.class);
        @SuppressWarnings("rawtypes")
        MethodResolver interfaceMethodResolver = null;
        if (methodResolving != null) {
            interfaceMethodResolver = methodResolverExtractor.getMethodResolver(event, listener, methodResolving);
        } else {
            setDefaultMethod(eventConfig, event, listener);
        }
        validateTargetAnnotation(eventAnnotation);
        AnnotatedMethodMethodResolver methodResolver = new AnnotatedMethodMethodResolver(eventAnnotation, interfaceMethodResolver);
        eventConfig.setMethodResolver(methodResolver);
        eventConfig.setEventAnnotation(eventAnnotation);
        return eventConfig;
        
    }

    
    /**
     * Find the default method to invoke on listener and sets it on the eventConfig.
     * 
     * @param eventConfig
     *            The EventConfig.
     * @param event
     *            Event class
     * @param listener
     *            Event Listener Interface class
     * 
     **/
    private void setDefaultMethod(EventConfigImpl eventConfig, Class<? extends EventObject> event,
                Class<? extends EventListener> listener) {
        if (ReflectionUtils.getDeclaredMethodCount(listener) == 1) {
            Set<Method> methodSet = ReflectionUtils.getDeclaredMethodsMatchingReturnType(listener, Void.TYPE, event);
            eventConfig.setDefaultMethod(methodSet.iterator().next());
        } else {
            throw new IllegalStateException("EventListener " + listener
                        + " should only have one method declared if not annotated with @MethodResolving");
        }
    }

    /**
     * Create a new event from an event class, interface class and the event listener method to invoke.
     * 
     * @param event
     *            Event class to create EventConfig for.
     * @param listener
     *            Event Listener Interface class to create EventConfig for.
     * @param eventMethod
     *            Method from listener class to invoke on event.
     * @param name
     *            Name of the event.
     * 
     * @return a new EventConfig.
     **/
    public EventConfig newWithEventMethod(Class<? extends EventObject> event, Class<? extends EventListener> listener,
                Method eventMethod, String name) {
        EventConfigImpl eventConfig = new EventConfigImpl(event, listener, name);
        ReflectionUtils.verifyMethodSignature(eventMethod, Void.TYPE, event);
        eventConfig.setDefaultMethod(eventMethod);
        return eventConfig;
    }

    /**
     * Create a new event from an event class, interface class and a MethodResolver.
     * 
     * @param event
     *            Event class to create EventConfig for.
     * @param listener
     *            Event Listener Interface class to create EventConfig for.
     * @param methodResolver
     *            Method Resolver to use.
     * @param name
     *            Name of the event.
     * @return a new EventConfig.
     **/
    public EventConfig newWithMethodResolver(Class<? extends EventObject> event,
                Class<? extends EventListener> listener, MethodResolver<EventObject> methodResolver, String name) {
        EventConfigImpl eventConfig = new EventConfigImpl(event, listener, name);
        if (methodResolver == null) {
            throw new IllegalArgumentException("methodResolver is not allowed to be null");
        }
        eventConfig.setMethodResolver(methodResolver);
        return eventConfig;
    }

    

}
