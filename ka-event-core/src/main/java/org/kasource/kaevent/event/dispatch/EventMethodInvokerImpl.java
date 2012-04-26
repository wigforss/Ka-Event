package org.kasource.kaevent.event.dispatch;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.EventObject;
import java.util.List;

import org.apache.log4j.Logger;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.filter.EventFilter;
import org.kasource.kaevent.event.filter.EventFilterExecutor;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.listener.register.EventListenerRegistration;

/**
 * Default implementation of the EventMethodInvoker.
 * 
 * @author wigforss
 * @version $Id$
 */
public class EventMethodInvokerImpl implements EventMethodInvoker {
    private static Logger logger = Logger.getLogger(EventMethodInvokerImpl.class);
    
   
    private EventRegister eventRegister;
    private EventFilterExecutor filterExecutor = new EventFilterExecutor();
  
    /**
     * Constructor.
     *   
     * @param eventRegister Event Register.
     **/
    public EventMethodInvokerImpl(EventRegister eventRegister) {
        this.eventRegister = eventRegister;
    }

    /**
     * Invokes event on all listeners.
     * 
     *  If blocked is true exceptions are thrown else exceptions are logged and suppressed. 
     * 
     * @param event     Event to invoke.
     * @param listeners Collection if listener to invoke the event method on.
     * @param throwException   Exception handling type, true throws exception and false suppresses exceptions.
     **/
    @Override
    public void invokeEventMethod(EventObject event, Collection<EventListenerRegistration> listeners, boolean throwException) {
        if (listeners != null && listeners.size() > 0) {
            EventConfig eventConfig = eventRegister.getEventByClass(event.getClass());
            
            for (EventListenerRegistration listener : listeners) {
                Method method = eventConfig.getEventMethod(event, listener.getListener());
                invokeEvent(event, throwException, method, listener);
            }
        }
    }

    /**
     * Invoke event on listener.
     * 
     * @param event     Event to send.
     * @param throwException   exception handling
     * @param method    Method to invoke.
     * @param listener  Listener.
     */
    private void invokeEvent(EventObject event, boolean throwException, Method method, EventListenerRegistration listener) {
        if (filterExecutor.passFilters(listener.getFilters(), event)) {
            if (throwException) {
                invokeBlocked(method, listener, event);
            } else {
                invoke(method, listener, event);
            }
        }
    }

    /**
     * Invoke event on listener, handle exceptions as blocked call.
     * 
     * @param method   Method to invoke.
     * @param listener Listener
     * @param event    Event to send.
     * @throws RuntimeException if any exception occurs when invoking the method.
     * @throws IllegalStateException if no event method to invoke could be resolved.
     */
    private void invokeBlocked(Method method, 
                               EventListenerRegistration listener, 
                               EventObject event) throws RuntimeException {
        try {
            if(method != null) {
                method.invoke(listener.getListener(), event);
            } else {
                throw new IllegalStateException("Could not resolve an event method for " + event.getClass() + ".");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke " + method + " on " + listener,
                    e instanceof InvocationTargetException ? e.getCause() : e);
        }
    }

    /**
     * Invoke event on listener, handle exceptions by just logging.
     * 
     * @param method        Method to invoke.
     * @param listener      Listener.
     * @param event         Event to send.
     */
    private void invoke(Method method, EventListenerRegistration listener, EventObject event) {
        
        try {
            if(method != null) {
                method.invoke(listener.getListener(), event);
            } else {
                logger.error("Could not resolve an event method for " + event.getClass() + ".");
            }
        } catch (Exception e) {
            logger.error("Failed to invoke " + method + " on " + listener, e instanceof InvocationTargetException ? e
                    .getCause() : e);
        }
    }

    

}
