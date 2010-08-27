/**
 * 
 */
package org.kasource.kaevent.event.dispatch;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.EventObject;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.filter.EventFilter;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.listener.register.EventListenerRegistration;

/**
 * @author wigforss
 * 
 */
public class EventMethodInvokerImpl implements EventMethodInvoker {
    private static Logger logger = Logger.getLogger(EventMethodInvokerImpl.class);
    
   
    private EventRegister eventRegister;

    public EventMethodInvokerImpl() {}
    
    public EventMethodInvokerImpl(EventRegister eventRegister) {
        this.eventRegister = eventRegister;
    }

    
    public void invokeEventMethod(EventObject event, Collection<EventListenerRegistration> listeners, boolean blocked) {
        if (listeners != null && listeners.size() > 0) {
            EventConfig eventConfig = eventRegister.getEventByClass(event.getClass());
            Method method = eventConfig.getEventMethod(event);
            for (EventListenerRegistration listener : listeners) {
                if (passFilters(listener, event)) {
                    if (blocked) {
                        invokeBlocked(method, listener, event);
                    } else {
                        invoke(method, listener, event);
                    }
                }
            }
        }
    }

    private void invokeBlocked(Method method, EventListenerRegistration listener, EventObject event) {
        try {
            method.invoke(listener.getListener(), event);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke " + method + " on " + listener,
                    e instanceof InvocationTargetException ? e.getCause() : e);
        }
    }

    private void invoke(Method method, EventListenerRegistration listener, EventObject event) {
        try {
            method.invoke(listener.getListener(), event);
        } catch (Exception e) {
            logger.error("Failed to invoke " + method + " on " + listener, e instanceof InvocationTargetException ? e
                    .getCause() : e);
        }
    }

    @SuppressWarnings("unchecked")
    private boolean passFilters(EventListenerRegistration eventRegistration, EventObject event) {
        if (eventRegistration.getFilters() != null) {
            for (EventFilter<? extends EventObject> filter : eventRegistration.getFilters()) {
                EventFilter<EventObject> eventFilter = (EventFilter<EventObject>) filter;
                if (!eventFilter.passFilter(event)) {
                    return false;
                }
            }
        }
        return true;
    }

}
