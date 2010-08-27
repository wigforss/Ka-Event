/**
 * 
 */
package org.kasource.kaevent.config;

import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.event.config.EventFactory;
import org.kasource.kaevent.event.dispatch.DispatcherQueueThread;
import org.kasource.kaevent.event.dispatch.EventMethodInvoker;
import org.kasource.kaevent.event.dispatch.EventSender;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegister;

/**
 * @author rikardwigforss
 *
 */
public interface KaEventConfiguration {


    /**
     * @return the eventDispatcher
     */
    public EventDispatcher getEventDispatcher();
    
    /**
     * @return the beanResolver
     */
    public abstract BeanResolver getBeanResolver();

    /**
     * @return the eventFactory
     */
    public abstract EventFactory getEventFactory();

    /**
     * @return the eventRegister
     */
    public abstract EventRegister getEventRegister();

    /**
     * @return the eventMethodinvoker
     */
    public abstract EventMethodInvoker getEventMethodInvoker();

    /**
     * @return the soListenerRegister
     */
    public abstract SourceObjectListenerRegister getSourceObjectListenerRegister();

    /**
     * @return the channelRegister
     */
    public abstract ChannelRegister getChannelRegister();

    /**
     * @return the eventSender
     */
    public abstract EventSender getEventSender();

    /**
     * @return the channelFactory
     */
    public abstract ChannelFactory getChannelFactory();

    /**
     * @return the queueThread
     */
    public abstract DispatcherQueueThread getQueueThread();

}