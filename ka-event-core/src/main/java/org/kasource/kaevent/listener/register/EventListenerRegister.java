/**
 * 
 */
package org.kasource.kaevent.listener.register;

import java.util.Collection;
import java.util.EventObject;


/**
 * @author rikardwigforss
 *
 */
public interface EventListenerRegister {


    public  Collection<EventListenerRegistration> getListenersByEvent(EventObject event);
    
  

}
