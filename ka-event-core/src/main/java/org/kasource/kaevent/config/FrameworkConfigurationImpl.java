/**
 * 
 */
package org.kasource.kaevent.config;

import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.channel.ChannelRegisterImpl;
import org.kasource.kaevent.event.config.EventConfigFactory;
import org.kasource.kaevent.event.dispatch.DispatcherQueueThread;
import org.kasource.kaevent.event.dispatch.EventMethodInvoker;
import org.kasource.kaevent.event.dispatch.EventSender;
import org.kasource.kaevent.event.register.DefaultEventRegister;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.listener.register.EventListenerRegister;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegister;

/**
 * @author rikardwigforss
 * 
 */
public class FrameworkConfigurationImpl implements FrameworkConfiguration {

    private BeanResolver beanResolver;

    private EventConfigFactory eventFactory;

    private EventRegister eventRegister;

    private EventMethodInvoker eventMethodinvoker;

    private SourceObjectListenerRegister soListenerRegister;

    private ChannelRegister channelRegister;

    private EventSender eventSender;

    private ChannelFactory channelFactory;

    private DispatcherQueueThread queueThread;

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

    public EventSender getEventSender() {
        return eventSender;
    }

    /**
     * @param eventSender
     *            the eventSender to set
     */
    public void setEventSender(EventSender eventSender) {
        this.eventSender = eventSender;
    }

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

}
