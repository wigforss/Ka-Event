/**
 * 
 */
package org.kasource.kaevent.listener.register;

import java.util.EventListener;
import java.util.EventObject;
import java.util.Set;

/**
 * @author rikardwigforss
 *
 */
public interface EventListenerRegister {


    public  Set<EventListener> getListenersByEvent(EventObject event);

}
