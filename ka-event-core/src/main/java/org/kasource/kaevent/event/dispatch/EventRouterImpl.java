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
	 * @param event		Event to route.
	 * @param blocked	true to invoke the method in a blocked fashion, else false.
	 **/
    @Override
    public void routeEvent(EventObject event, boolean blocked) {
        Set<Channel> channels = channelRegister.getChannelsByEvent(event.getClass());
        if (channels != null) {
            for (Channel channel : channels) {
                channel.fireEvent(event, blocked);
            }
        }
        Collection<EventListenerRegistration> listeners = sourceObjectListenerRegister.getListenersByEvent(event);
        
        invoker.invokeEventMethod(event, listeners, blocked);
    }
}
