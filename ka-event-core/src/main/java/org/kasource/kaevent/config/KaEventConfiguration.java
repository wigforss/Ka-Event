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
import org.kasource.kaevent.event.dispatch.EventRouter;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegister;

/**
 * The result of running the configuration.
 * 
 * This object holds the configured ka-event environment. So it can be accessed 
 * after the configuration code has been ran.
 * 
 * @author rikardwigforss
 * @version $Id$
 **/
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
     * @return the eventRouter
     */
    public abstract EventRouter getEventRouter();

    /**
     * @return the channelFactory
     */
    public abstract ChannelFactory getChannelFactory();

    /**
     * @return the queueThread
     */
    public abstract DispatcherQueueThread getQueueThread();

}