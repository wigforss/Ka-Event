package org.kasource.kaevent.config;

import java.lang.annotation.Annotation;
import java.util.EventListener;
import java.util.EventObject;

import org.kasource.kaevent.spring.context.event.ContextClosedListener;
import org.kasource.kaevent.spring.context.event.ContextRefreshedListener;
import org.kasource.kaevent.spring.context.event.ContextStartedListener;
import org.kasource.kaevent.spring.context.event.ContextStoppedListener;
import org.kasource.kaevent.spring.context.event.OnContextClosed;
import org.kasource.kaevent.spring.context.event.OnContextRefreshed;
import org.kasource.kaevent.spring.context.event.OnContextStarted;
import org.kasource.kaevent.spring.context.event.OnContextStopped;
import org.kasource.kaevent.spring.web.context.OnRequestHandled;
import org.kasource.kaevent.spring.web.context.OnServletRequestHandled;
import org.kasource.kaevent.spring.web.context.RequestHandledListener;
import org.kasource.kaevent.spring.web.context.ServletRequestHandledListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.web.context.support.RequestHandledEvent;
import org.springframework.web.context.support.ServletRequestHandledEvent;

public enum SpringEvent {
    CONTEXT_CLOSED (ContextClosedEvent.class, ContextClosedListener.class, OnContextClosed.class),
    CONTEXT_REFRESHED (ContextRefreshedEvent.class, ContextRefreshedListener.class, OnContextRefreshed.class),
    CONTEXT_STARTED (ContextStartedEvent.class, ContextStartedListener.class, OnContextStarted.class),
    CONTEXT_STOPPED (ContextStoppedEvent.class, ContextStoppedListener.class, OnContextStopped.class),
    REQUEST_HANDLED (RequestHandledEvent.class, RequestHandledListener.class, OnRequestHandled.class),
    SERVLET_REQUEST_HANDLED (ServletRequestHandledEvent.class, ServletRequestHandledListener.class, OnServletRequestHandled.class);
    
    private Class<? extends EventObject> event;
    private Class<? extends EventListener> listener;
    private Class<? extends Annotation> methodAnnotation;
    
    SpringEvent(Class<? extends EventObject> event, Class<? extends EventListener> listener, Class<? extends Annotation> methodAnnotation) {
        this.event = event;
        this.listener = listener;
        this.methodAnnotation = methodAnnotation;
    }

    /**
     * @return the event
     */
    protected Class<? extends EventObject> getEvent() {
        return event;
    }

    /**
     * @return the listener
     */
    protected Class<? extends EventListener> getListener() {
        return listener;
    }
    
    /**
     * @return the method annotation.
     */
    protected Class<? extends Annotation> getMethodAnnotation() {
        return methodAnnotation;
    }

  
}
