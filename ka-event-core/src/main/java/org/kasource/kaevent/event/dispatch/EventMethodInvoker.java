package org.kasource.kaevent.event.dispatch;

import java.util.Collection;
import java.util.EventObject;

import org.kasource.kaevent.listener.register.EventListenerRegistration;

/**
 * Class that invokes the event listener interface method on the listeners.
 * 
 * @author rikardwigforss
 * @version $Id$
 **/
public interface EventMethodInvoker {

    /**
     * Invokes event on all listeners.
     * 
     *  If blocked is true exceptions are thrown else exceptions are logged and suppressed. 
     * 
     * @param event     Event to invoke.
     * @param listeners Collection if listener to invoke the event method on.
     * @param blocked   Exception handling type, true throws exception and false suppresses exceptions.
     **/
    public abstract void invokeEventMethod(EventObject event, Collection<EventListenerRegistration> listeners,
            boolean blocked);

}
