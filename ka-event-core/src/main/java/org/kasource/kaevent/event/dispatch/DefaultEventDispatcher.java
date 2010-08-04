/**
 * 
 */
package org.kasource.kaevent.event.dispatch;

import java.util.EventListener;
import java.util.EventObject;

import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.config.FrameworkConfigurer;
import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegister;

/**
 * @author wigforss
 *
 */
public class DefaultEventDispatcher implements EventDispatcher{

    private EventSender eventSender;
    private ChannelRegister channelRegister;
    private ChannelFactory channelFactory;
    private SourceObjectListenerRegister sourceObjectListenerRegister;
    private DispatcherQueueThread eventQueue;
    
    
    public DefaultEventDispatcher(String scanPath) {
        initialize(scanPath);
    }
    
    public DefaultEventDispatcher() {
        initialize(null);
    }
    
    private void initialize(String scanPath) {
        new FrameworkConfigurer().configure(this, scanPath);
    }
    
    
    @Override
    public void fire(EventObject event) {
        eventQueue.enqueue(event);        
    }

   
    @Override
    public void fireBlocked(EventObject event) {
        eventSender.dispatchEvent(event, false);       
    }

  
    @Override
    public void fireOnCommit(EventObject event) {
        throw new IllegalStateException("Not implemented on "+this.getClass());
        
    }

    @Override
    public Channel createChannel(String channelName) {
        return channelFactory.createChannel(channelName);
    }
 
    @Override
    public ChannelRegister getChannelRegister() { 
        return channelRegister;
    }

    @Override
    public void registerListener(EventListener listener,
            Object sourceObject) {
        sourceObjectListenerRegister.registerListener(listener, sourceObject);
    }

    /**
     * @param channelRegister the channelRegister to set
     */
    public void setChannelRegister(ChannelRegister channelRegister) {
        this.channelRegister = channelRegister;
    }

    /**
     * @param sourceObjectListenerRegister the sourceObjectListenerRegister to set
     */
    public void setSourceObjectListenerRegister(SourceObjectListenerRegister sourceObjectListenerRegister) {
        this.sourceObjectListenerRegister = sourceObjectListenerRegister;
    }

    /**
     * @param eventQueue the eventQueue to set
     */
    public void setEventQueue(DispatcherQueueThread eventQueue) {
        this.eventQueue = eventQueue;
    }

    /**
     * @param eventSender the eventSender to set
     */
    public void setEventSender(EventSender eventSender) {
        this.eventSender = eventSender;
    }

    /**
     * @param channelFactory the channelFactory to set
     */
    public void setChannelFactory(ChannelFactory channelFactory) {
        this.channelFactory = channelFactory;
    }

 
    

}
