/**
 * 
 */
package org.kasource.kaevent.event.dispatch;

import java.util.Collection;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Set;

import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.listener.register.EventListenerRegister;
import org.kasource.kaevent.listener.register.EventListenerRegistration;


/**
 * @author rikardwigforss
 *
 */
public class EventSender {
    private ChannelRegister channelRegister;
    private EventListenerRegister sourceObjectListenerRegister;
    private EventMethodInvoker invoker;
    
    public EventSender(ChannelRegister channelRegister, EventListenerRegister sourceObjectListenerRegister, EventMethodInvoker invoker) {
        this.channelRegister = channelRegister;
        this.sourceObjectListenerRegister = sourceObjectListenerRegister;
        this.invoker = invoker;
    }
    
    
    public void dispatchEvent(EventObject event, boolean blocked) {
        Set<Channel> channels = channelRegister.getChannelsByEvent(event.getClass());
        if(channels != null) {
            for(Channel channel : channels) {
                channel.fireEvent(event, blocked);
            }
        }
        Collection<EventListenerRegistration> listeners = sourceObjectListenerRegister.getListenersByEvent(event);
        
        invoker.invokeEventMethod(event, listeners, blocked);
    }
}
