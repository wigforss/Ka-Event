/**
 * 
 */
package org.kasource.kaevent.event.config;

import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Set;

import org.kasource.commons.util.ReflectionUtils;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.event.Event;
import org.kasource.kaevent.event.method.MethodResolver;
import org.kasource.kaevent.listener.interfaces.MethodResolving;

/**
 * Create EventConfig objects
 * 
 * An EventConfig object must have either an defaultMethod or a methodResolver
 * 
 * @author rikardwigforss
 * 
 */
public class EventConfigFactory {

    public EventConfigFactory(BeanResolver beanResolver) {
        methodResolverExtractor = new AnnotationMethodResolverExtractor(beanResolver);
    }

    private AnnotationMethodResolverExtractor methodResolverExtractor;

    
    public EventConfig newFromAnnotatedEventClass(Class<? extends EventObject> event) {
        Event eventAnnotation = event.getAnnotation(Event.class);
        if(eventAnnotation == null) {
            throw new IllegalArgumentException(event+" is not annotated with @Event!");
        }
       return  newFromAnnotatedInterfaceClass(event, event.getAnnotation(Event.class).listener(), event.getName());
    }
    
    /**
     * Create and an EventConfig based on an annotated event class
     * 
     * @param event
     * @return
     **/
    public EventConfig newFromAnnotatedInterfaceClass(Class<? extends EventObject> event, Class<? extends EventListener> listener, String name) {
        EventConfigImpl eventConfig = new EventConfigImpl(event, listener, name);
        MethodResolving methodResolving = listener.getAnnotation(MethodResolving.class);
        if (methodResolving != null) {
            eventConfig.methodResolver = methodResolverExtractor.getMethodResolver(event, listener, methodResolving);
        } else {
                setDefaultMethod(eventConfig, event, listener);
        }
        return eventConfig;
    }

    private void setDefaultMethod(EventConfigImpl eventConfig, Class<? extends EventObject> event,
            Class<? extends EventListener> listener) {
        if (ReflectionUtils.getDeclaredMethodCount(listener) == 1) {
            Set<Method> methodSet = ReflectionUtils.getDeclaredMethodsMatchingReturnType(listener, Void.TYPE, event );
            eventConfig.defaultMethod = methodSet.iterator().next();
        } else {
        	throw new IllegalStateException("EventListener " + listener + " should only have one method declared if not annotated with @MethodResolving"); 
        }
    }

    public EventConfig newWithEventMethod(Class<? extends EventObject> event, Class<? extends EventListener> listener,
            Method eventMethod, String name) {
    	EventConfigImpl eventConfig = new EventConfigImpl(event, listener, name);
        ReflectionUtils.verifyMethodSignature(eventMethod, Void.TYPE, event);
        eventConfig.defaultMethod = eventMethod;
        return eventConfig;
    }

    @SuppressWarnings("unchecked")
    public EventConfig newWithMethodResolver(Class<? extends EventObject> event, Class<? extends EventListener> listener,
            MethodResolver methodResolver, String name) {
    	EventConfigImpl eventConfig = new EventConfigImpl(event, listener, name);
        if(methodResolver == null) {
        	throw new IllegalArgumentException("methodResolver is not allowed to be null");
        }
        eventConfig.methodResolver = methodResolver;
        return eventConfig;
    }

}
