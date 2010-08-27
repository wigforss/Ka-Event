/**
 * 
 */
package org.kasource.kaevent.event.dispatch;

import java.util.Collection;
import java.util.EventObject;
import java.util.Set;

import javax.annotation.Resource;

import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.listener.register.EventListenerRegistration;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegister;


/**
 * @author Rikard Wigforss
 *
 */
public class EventSenderImpl implements EventSender {
      
    private ChannelRegister channelRegister;
    
    private SourceObjectListenerRegister sourceObjectListenerRegister;
    
    private EventMethodInvoker invoker;
    
    protected EventSenderImpl(){}
    
    public EventSenderImpl(ChannelRegister channelRegister, SourceObjectListenerRegister sourceObjectListenerRegister, EventMethodInvoker invoker) {
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
