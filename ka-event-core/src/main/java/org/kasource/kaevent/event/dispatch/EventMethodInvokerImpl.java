package org.kasource.kaevent.event.dispatch;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.EventObject;

import org.apache.log4j.Logger;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.filter.EventFilter;
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
            Method method = eventConfig.getEventMethod(event);
            for (EventListenerRegistration listener : listeners) {
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
        if (passFilters(listener, event)) {
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
     */
    private void invokeBlocked(Method method, 
                               EventListenerRegistration listener, 
                               EventObject event) throws RuntimeException {
        try {
            method.invoke(listener.getListener(), event);
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
            method.invoke(listener.getListener(), event);
        } catch (Exception e) {
            logger.error("Failed to invoke " + method + " on " + listener, e instanceof InvocationTargetException ? e
                    .getCause() : e);
        }
    }

    /**
     * Return true if event passed the filters from eventRegistration.
     * 
     * @param eventRegistration Event Registration, holds the filters.
     * @param event             Event Object to test.
     * 
     * @return true if event passes filters, else false.
     */
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
