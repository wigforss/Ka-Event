/**
 * 
 */
package org.kasource.kaevent.event.dispatch;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Set;

import javax.swing.event.ChangeEvent;

import org.apache.log4j.Logger;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.register.EventRegister;

/**
 * @author wigforss
 * 
 */
public class EventMethodInvoker {
    private static Logger logger = Logger.getLogger(EventMethodInvoker.class);
    private EventRegister eventRegister;

    public EventMethodInvoker(EventRegister eventRegister) {
        this.eventRegister = eventRegister;
    }
    
    public void invokeEventMethod(EventObject event, Set<EventListener> listeners, boolean blocked) {
        if (listeners != null && listeners.size() > 0) {
            EventConfig eventConfig = eventRegister.getEventByClass(event.getClass());
            Method method = eventConfig.getEventMethod(event);
            for (EventListener listener : listeners) {
                if (blocked) {
                    invokeBlocked(method, listener, event);
                } else {
                    invoke(method, listener, event);
                }
            }
        }
    }

    private void invokeBlocked(Method method, EventListener listener, EventObject event) {
        try {
            method.invoke(listener, event);
        }  catch (Exception e) {
            throw new RuntimeException("Failed to invoke " + method + " on " + listener, e instanceof InvocationTargetException ? e.getCause() : e);
        } 
    }

    private void invoke(Method method, EventListener listener, EventObject event) {
        try {
            method.invoke(listener, event);
        } catch (Exception e) {
            logger.error("Failed to invoke " + method + " on " + listener, e instanceof InvocationTargetException ? e.getCause() : e);
        }
    }

    
    

}
