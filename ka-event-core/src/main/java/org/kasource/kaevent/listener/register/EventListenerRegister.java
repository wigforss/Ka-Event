/**
 * 
 */
package org.kasource.kaevent.listener.register;

import java.util.Collection;
import java.util.EventObject;


/**
 * Event Listener Register.
 * 
 * @author rikardwigforss
 */
public interface EventListenerRegister {

    /**
     * Returns all event listener registration by an event.
     * 
     * @param event Event to get listeners for.
     * 
     * @return All Listeners for an event.
     **/
    public  Collection<EventListenerRegistration> getListenersByEvent(EventObject event);
    
  

}
