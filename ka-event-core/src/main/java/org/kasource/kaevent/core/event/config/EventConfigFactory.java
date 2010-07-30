/**
 * 
 */
package org.kasource.kaevent.core.event.config;

import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Set;

import org.kasource.commons.util.ReflectionUtils;
import org.kasource.kaevent.core.event.Event;
import org.kasource.kaevent.core.event.method.MethodResolver;
import org.kasource.kaevent.core.listener.interfaces.MethodResolving;

/**
 * @author rikardwigforss
 * 
 */
public class EventConfigFactory {


    private AnnotationMethodResolverExtractor methodResolverExtractor;

    /**
     * Create and an EventConfig based on an annotated event class
     * 
     * @param event
     * @return
     **/
    public EventConfig createEventConfig(Class<? extends EventObject> event) {
        Event eventAnnotation = event.getAnnotation(Event.class);
        if(event == null) {
            throw new IllegalArgumentException(event+" is not annotated with @Event!");
        }
        Class<? extends EventListener> listener  = eventAnnotation.listener();
        EventConfigImpl eventConfig = new EventConfigImpl(event, listener);
        MethodResolving methodResolving = listener.getAnnotation(MethodResolving.class);
        if (methodResolving != null) {
            eventConfig.setMethodResolver(methodResolverExtractor.getMethodResolver(event, listener, methodResolving));
        } else {
            setEventMethod(eventConfig, event, listener);
        }
        eventConfig.setChannels(eventAnnotation.channels());
        return eventConfig;
    }

    private void setEventMethod(EventConfigImpl eventConfig, Class<? extends EventObject> event,
            Class<? extends EventListener> listener) {
        if (ReflectionUtils.getMethodCount(listener) == 1) {
            Set<Method> methodSet = ReflectionUtils.getMethods(listener, Void.TYPE, new Class<?>[] { event });
            eventConfig.setEventMethod(methodSet.iterator().next());
        }
    }

    public EventConfig createEventConfig(Class<? extends EventObject> event, Class<? extends EventListener> listener,
            Method eventMethod, String[] channels) {
        EventConfigImpl eventConfig = new EventConfigImpl(event, listener);
        ReflectionUtils.verifyMethodSignature(eventMethod, Void.TYPE, event);
        eventConfig.setEventMethod(eventMethod);
        eventConfig.setChannels(channels);
        return eventConfig;
    }

    public EventConfig createEventConfig(Class<? extends EventObject> event, Class<? extends EventListener> listener,
            MethodResolver<? extends EventObject> methodResolver, String[] channels) {
        EventConfigImpl eventConfig = new EventConfigImpl(event, listener);
        eventConfig.setMethodResolver(methodResolver);
        eventConfig.setChannels(channels);
        return eventConfig;
    }

}
