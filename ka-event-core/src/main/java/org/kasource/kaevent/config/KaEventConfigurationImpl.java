package org.kasource.kaevent.config;

import java.util.Map;

import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.event.config.EventBuilderFactory;
import org.kasource.kaevent.event.dispatch.DispatcherQueueThread;
import org.kasource.kaevent.event.dispatch.EventMethodInvoker;
import org.kasource.kaevent.event.dispatch.EventQueueRegister;
import org.kasource.kaevent.event.dispatch.EventRouter;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegister;

/**
 * The default KaEventConfiguration implementation.
 * 
 * Provides access to the configured ka-event environment.
 * 
 * @author Rikard Wigforss
 * @version $Id$
 */
public class KaEventConfigurationImpl implements KaEventConfiguration {
 
    private EventDispatcher eventDispatcher;
    
    private BeanResolver beanResolver;

    private EventBuilderFactory eventBuilderFactory;

    private EventRegister eventRegister;

    private EventMethodInvoker eventMethodInvoker;

    private SourceObjectListenerRegister sourceObjectListenerRegister;
    
    private ChannelRegister channelRegister;

    private EventRouter eventRouter;

    private ChannelFactory channelFactory;

    private DispatcherQueueThread defaultEventQueue;
    
    private EventQueueRegister eventQueueRegister;

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
    public EventBuilderFactory getEventBuilderFactory() {
        return eventBuilderFactory;
    }

    /**
     * @param eventBuilderFactory
     *            the eventBuilderFactory to set
     */
    public void setEventBuilderFactory(EventBuilderFactory eventBuilderFactory) {
        this.eventBuilderFactory = eventBuilderFactory;
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
     * Set the event method invoker.
     * 
     * @param eventMethodInvoker
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
     * Set the source object listener register.
     * 
     * @param sourceObjectListenerRegister
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
    public DispatcherQueueThread getDefaultEventQueue() {
        return defaultEventQueue;
    }

    /**
     * @param eventQueue
     *            the eventQueue to set
     */
    public void setDefaultEventQueue(DispatcherQueueThread eventQueue) {
        this.defaultEventQueue = eventQueue;
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

    /**
     * @return the eventQueueRegister
     */
    public EventQueueRegister getEventQueueRegister() {
        return eventQueueRegister;
    }

    /**
     * @param eventQueueRegister the eventQueueRegister to set
     */
    public void setEventQueueRegister(EventQueueRegister eventQueueRegister) {
        this.eventQueueRegister = eventQueueRegister;
    }

    
}
