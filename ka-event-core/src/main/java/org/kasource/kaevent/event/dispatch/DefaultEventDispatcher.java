/**
 * 
 */
package org.kasource.kaevent.event.dispatch;

import java.util.EventListener;
import java.util.EventObject;
import java.util.List;

import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.config.FrameworkConfiguration;
import org.kasource.kaevent.config.FrameworkConfigurer;
import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.event.filter.EventFilter;
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
    private FrameworkConfigurer configurer = new FrameworkConfigurer();
    private FrameworkConfiguration config;
    
    public DefaultEventDispatcher(String scanPath) {
        initialize(scanPath);
    }
    
    public DefaultEventDispatcher() {
        initialize(null);
    }
    
    protected void initialize(String scanPath) {
        this.config = configurer.configure(scanPath);
        
        this.channelRegister = config.getChannelRegister();
        this.sourceObjectListenerRegister = config.getSoListenerRegister();
        this.eventQueue = config.getQueueThread();
        this.eventSender = config.getEventSender();
        this.channelFactory = config.getChannelFactory();
    }
    
   
    
    @Override
    public void fire(EventObject event) {
        eventQueue.enqueue(event);        
    }

   
    @Override
    public void fireBlocked(EventObject event) {
        eventSender.dispatchEvent(event, true);       
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
    public Channel getChannel(String channelName) { 
        return channelRegister.getChannel(channelName);
    }

    @Override
    public void registerListener(EventListener listener,
            Object sourceObject) {
        sourceObjectListenerRegister.registerListener(listener, sourceObject);
    }

    
    @Override
    public void registerListener(EventListener listener, Object sourceObject, List<EventFilter<EventObject>> filters) {
        sourceObjectListenerRegister.registerListener(listener, sourceObject);
        
    }
    
    
    
    
    
   

    
    

 
    

}
