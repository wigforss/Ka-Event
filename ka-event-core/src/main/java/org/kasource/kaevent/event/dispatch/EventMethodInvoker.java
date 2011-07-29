/**
 * 
 */
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

    public abstract void invokeEventMethod(EventObject event, Collection<EventListenerRegistration> listeners,
            boolean blocked);

}