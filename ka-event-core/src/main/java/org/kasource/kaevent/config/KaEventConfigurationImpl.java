/**
 * 
 */
package org.kasource.kaevent.config;

import javax.annotation.Resource;

import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.event.config.EventConfigFactory;
import org.kasource.kaevent.event.dispatch.DispatcherQueueThread;
import org.kasource.kaevent.event.dispatch.EventMethodInvoker;
import org.kasource.kaevent.event.dispatch.EventSenderImpl;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegister;

/**
 * @author Rikard Wigforss
 * 
 */
public class KaEventConfigurationImpl implements KaEventConfiguration {

    @Resource
    private EventDispatcher eventDispatcher;
    
    @Resource
    private BeanResolver beanResolver;

    @Resource
    private EventConfigFactory eventFactory;

    @Resource
    private EventRegister eventRegister;

    @Resource
    private EventMethodInvoker eventMethodinvoker;

    @Resource
    private SourceObjectListenerRegister soListenerRegister;

    @Resource
    private ChannelRegister channelRegister;

    @Resource
    private EventSenderImpl eventSender;

    @Resource
    private ChannelFactory channelFactory;

    @Resource
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
    public EventConfigFactory getEventFactory() {
        return eventFactory;
    }

    /**
     * @param eventFactory
     *            the eventFactory to set
     */
    public void setEventFactory(EventConfigFactory eventFactory) {
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
    public EventMethodInvoker getEventMethodinvoker() {
        return eventMethodinvoker;
    }

    /**
     * @param eventMethodinvoker
     *            the eventMethodinvoker to set
     */
    public void setEventMethodinvoker(EventMethodInvoker eventMethodinvoker) {
        this.eventMethodinvoker = eventMethodinvoker;
    }

    @Override
    public SourceObjectListenerRegister getSoListenerRegister() {
        return soListenerRegister;
    }

    /**
     * @param soListenerRegister
     *            the soListenerRegister to set
     */
    public void setSoListenerRegister(SourceObjectListenerRegister soListenerRegister) {
        this.soListenerRegister = soListenerRegister;
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
    public EventSenderImpl getEventSender() {
        return eventSender;
    }

    /**
     * @param eventSender
     *            the eventSender to set
     */
    public void setEventSender(EventSenderImpl eventSender) {
        this.eventSender = eventSender;
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
