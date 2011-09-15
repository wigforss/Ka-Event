package org.kasource.kaevent.config;

import java.util.EventListener;
import java.util.EventObject;

import org.kasource.kaevent.spring.context.event.ContextClosedListener;
import org.kasource.kaevent.spring.context.event.ContextRefreshedListener;
import org.kasource.kaevent.spring.context.event.ContextStartedListener;
import org.kasource.kaevent.spring.context.event.ContextStoppedListener;
import org.kasource.kaevent.spring.web.context.RequestHandledListener;
import org.kasource.kaevent.spring.web.context.ServletRequestHandledListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.web.context.support.RequestHandledEvent;
import org.springframework.web.context.support.ServletRequestHandledEvent;

public enum SpringEvent {
    CONTEXT_CLOSED (ContextClosedEvent.class, ContextClosedListener.class),
    CONTEXT_REFRESHED (ContextRefreshedEvent.class, ContextRefreshedListener.class),
    CONTEXT_STARTED (ContextStartedEvent.class, ContextStartedListener.class),
    CONTEXT_STOPPED (ContextStoppedEvent.class, ContextStoppedListener.class),
    REQUEST_HANDLED (RequestHandledEvent.class, RequestHandledListener.class),
    SERVLET_REQUEST_HANDLED (ServletRequestHandledEvent.class, ServletRequestHandledListener.class);
    
    private Class<? extends EventObject> event;
    private Class<? extends EventListener> listener;
   
    
    SpringEvent(Class<? extends EventObject> event, Class<? extends EventListener> listener) {
        this.event = event;
        this.listener = listener;
       
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

  
}
