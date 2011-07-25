/**
 * 
 */
package org.kasource.kaevent.config;

import javax.annotation.Resource;

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
 * @author Rikard Wigforss
 * 
 */
public class KaEventConfigurationImpl implements KaEventConfiguration {
 
    private EventDispatcher eventDispatcher;
    
    private BeanResolver beanResolver;

    private EventFactory eventFactory;

    private EventRegister eventRegister;

    private EventMethodInvoker eventMethodInvoker;

    private SourceObjectListenerRegister sourceObjectListenerRegister;
    
    private ChannelRegister channelRegister;

    private EventRouter eventRouter;

    private ChannelFactory channelFactory;

    private DispatcherQueueThread queueThread;

    @Override
    public BeanResolver getBeanResolver() {
        return beanResolver;
    }

    /**
     * @param beanResolver
     *            the beanResolver to set
     */
    public void setBeanResolver(BeanResolver beanResolver) {
        this.beanResolver = beanResolver;
    }

    @Override
    public EventFactory getEventFactory() {
        return eventFactory;
    }

    /**
     * @param eventFactory
     *            the eventFactory to set
     */
    public void setEventFactory(EventFactory eventFactory) {
        this.eventFactory = eventFactory;
    }

    @Override
    public EventRegister getEventRegister() {
        return eventRegister;
    }

    /**
     * @param eventRegister
     *            the eventRegister to set
     */
    public void setEventRegister(EventRegister eventRegister) {
        this.eventRegister = eventRegister;
    }

    @Override
    public EventMethodInvoker getEventMethodInvoker() {
        return eventMethodInvoker;
    }

    /**
     * @param eventMethodinvoker
     *            the eventMethodinvoker to set
     */
    public void setEventMethodInvoker(EventMethodInvoker eventMethodInvoker) {
        this.eventMethodInvoker = eventMethodInvoker;
    }

    @Override
    public SourceObjectListenerRegister getSourceObjectListenerRegister() {
        return sourceObjectListenerRegister;
    }

    /**
     * @param soListenerRegister
     *            the soListenerRegister to set
     */
    public void setSourceObjectListenerRegister(SourceObjectListenerRegister sourceObjectListenerRegister) {
        this.sourceObjectListenerRegister = sourceObjectListenerRegister;
    }

    @Override
    public ChannelRegister getChannelRegister() {
        return channelRegister;
    }

    /**
     * @param channelRegister
     *            the channelRegister to set
     */
    public void setChannelRegister(ChannelRegister channelRegister) {
        this.channelRegister = channelRegister;
    }

    @Override
    public EventRouter getEventRouter() {
        return eventRouter;
    }

    /**
     * @param eventRouter
     *            the eventRouter to set
     */
    public void setEventRouter(EventRouter eventRouter) {
        this.eventRouter = eventRouter;
    }

    @Override
    public ChannelFactory getChannelFactory() {
        return channelFactory;
    }

    /**
     * @param channelFactory
     *            the channelFactory to set
     */
    public void setChannelFactory(ChannelFactory channelFactory) {
        this.channelFactory = channelFactory;
    }

    @Override
    public DispatcherQueueThread getQueueThread() {
        return queueThread;
    }

    /**
     * @param queueThread
     *            the queueThread to set
     */
    public void setQueueThread(DispatcherQueueThread queueThread) {
        this.queueThread = queueThread;
    }

    /**
     * @return the eventDispatcher
     */
    @Override
    public EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

    /**
     * @param eventDispatcher the eventDispatcher to set
     */
    public void setEventDispatcher(EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

}
