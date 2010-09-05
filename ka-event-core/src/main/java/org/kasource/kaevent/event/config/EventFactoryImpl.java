/**
 * 
 */
package org.kasource.kaevent.event.config;

import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.kasource.commons.reflection.ReflectionUtils;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.event.Event;
import org.kasource.kaevent.event.method.MethodResolver;
import org.kasource.kaevent.listener.interfaces.MethodResolving;

/**
 * Create EventConfig objects
 * 
 * An EventConfig object must have either an defaultMethod or a methodResolver
 * 
 * @author Rikard Wigforss
 * 
 */
public class EventFactoryImpl implements EventFactory {

    

    private BeanResolver beanResolver;
    
    protected EventFactoryImpl() {    
    }
    
    public EventFactoryImpl(BeanResolver beanResolver) {
        methodResolverExtractor = new AnnotationMethodResolverExtractor(beanResolver);
    }

    @SuppressWarnings("unused")
    @PostConstruct
    private void initMethodResolverExtractor() {
        methodResolverExtractor = new AnnotationMethodResolverExtractor(beanResolver);
    }
    
    private AnnotationMethodResolverExtractor methodResolverExtractor;

  
    public EventConfig newFromAnnotatedEventClass(Class<? extends EventObject> event) {
       return  newFromAnnotatedEventClass(event, event.getName());
    }
    
    public EventConfig newFromAnnotatedEventClass(Class<? extends EventObject> event, String name) {
        Event eventAnnotation = event.getAnnotation(Event.class);
        if(eventAnnotation == null) {
            throw new IllegalArgumentException(event+" is not annotated with @Event!");
        }
       return  newFromAnnotatedInterfaceClass(event, event.getAnnotation(Event.class).listener(),name);
    }
    
   
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
