/**
 * 
 */
package org.kasource.kaevent.event.dispatch;

import java.util.Collection;
import java.util.EventObject;
import java.util.Set;

import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.listener.register.EventListenerRegistration;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegister;


/**
 * Default implementation of the EventRouter.
 * 
 * @author Rikard Wigforss
 * @version $Id$
 */
public class EventRouterImpl implements EventRouter {
      
    private ChannelRegister channelRegister;
    
    private SourceObjectListenerRegister sourceObjectListenerRegister;
    
    private EventMethodInvoker invoker;
    
    private int numberOfEventsInHistory = 100;
    
    private EventMap eventsFired = new EventMap(numberOfEventsInHistory);
   
    private boolean loopProtection = true;
    
   /**
    * Constructor.
    *  
    * @param channelRegister                Channel Register.
    * @param sourceObjectListenerRegister   Source Object Listener Register.
    * @param invoker                        Event Method Invoker.
    **/
    public EventRouterImpl(ChannelRegister channelRegister, 
    					   SourceObjectListenerRegister sourceObjectListenerRegister, 
    					   EventMethodInvoker invoker) {
        this.channelRegister = channelRegister;
        this.sourceObjectListenerRegister = sourceObjectListenerRegister;
        this.invoker = invoker;
    }
    
    /**
	 * Route the event to the correct destination.
	 * 
	 * If loop protection is enabled only events not seen before
	 * in history will be fired.
	 * 
	 * @param event		Event to route.
	 * @param throwException	true to invoke the method in a blocked fashion, else false.
	 **/
    @Override
    public void routeEvent(EventObject event, boolean throwException) {   
        if(loopProtection) {
            if(eventsFired.containsKey(event)) {
                return;
            } else {
                eventsFired.put(event, null);
            }
        }
        Set<Channel> channels = channelRegister.getChannelsByEvent(event.getClass());
        if (channels != null) {
            for (Channel channel : channels) {
               
                    channel.fireEvent(event, throwException);
                
            }
        }
        Collection<EventListenerRegistration> listeners = sourceObjectListenerRegister.getListenersByEvent(event);
        
        invoker.invokeEventMethod(event, listeners, throwException);
    }

    /**
     * @return the loopProtection
     */
    public boolean isLoopProtection() {
        return loopProtection;
    }

    /**
     * @param loopProtection the loopProtection to set
     */
    public void setLoopProtection(boolean loopProtection) {
        this.loopProtection = loopProtection;
    }
}
