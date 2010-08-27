/**
 * 
 */
package org.kasource.kaevent.event.dispatch;

import java.util.EventListener;
import java.util.EventObject;
import java.util.List;

import javax.annotation.Resource;

import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.config.KaEventInitializer;
import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.event.filter.EventFilter;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegister;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;

/**
 * @author Rikard Wigforss
 * 
 */

public class SpringEventDispatcher implements EventDispatcher {

   
    private ChannelRegister channelRegister;

    private ChannelFactory channelFactory;

    private SourceObjectListenerRegister sourceObjectListenerRegister;

    private DispatcherQueueThread eventQueue;

    private EventSender eventSender;

    /**
     * Used when configured in XML
     * 
     * @param channelRegister
     * @param channelFactory
     * @param sourceObjectListenerRegister
     * @param eventQueue
     * @param eventSender
     */
    private SpringEventDispatcher(ChannelRegister channelRegister, 
    							  ChannelFactory channelFactory,
    							  SourceObjectListenerRegister sourceObjectListenerRegister, 
    							  DispatcherQueueThread eventQueue,
    							  EventSender eventSender) {
        this.channelFactory = channelFactory;
        this.channelRegister = channelRegister;
        this.sourceObjectListenerRegister = sourceObjectListenerRegister;
        this.eventQueue = eventQueue;
        this.eventSender = eventSender;
    }

    @Override
    public Channel createChannel(String channelName) {
        return channelFactory.createChannel(channelName);
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
        // TODO Auto-generated method stub

    }

    @Override
    public Channel getChannel(String channelName) {
        return channelRegister.getChannel(channelName);
    }

    @Override
    public void registerListener(EventListener listener, Object sourceObject) {
        sourceObjectListenerRegister.registerListener(listener, sourceObject);

    }

    @Override
    public void registerListener(EventListener listener, Object sourceObject, List<EventFilter<EventObject>> filters) {
        sourceObjectListenerRegister.registerListener(listener, sourceObject, filters);

    }

	

}
