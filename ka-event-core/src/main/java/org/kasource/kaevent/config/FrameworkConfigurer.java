/**
 * 
 */
package org.kasource.kaevent.config;

import org.kasource.kaevent.bean.DefaultBeanResolver;
import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.channel.ChannelRegisterImpl;
import org.kasource.kaevent.event.config.EventConfigFactory;
import org.kasource.kaevent.event.dispatch.DefaultEventDispatcher;
import org.kasource.kaevent.event.dispatch.EventMethodInvoker;
import org.kasource.kaevent.event.dispatch.EventSender;
import org.kasource.kaevent.event.dispatch.ThreadPoolQueueExecutor;
import org.kasource.kaevent.event.export.AnnotationEventExporter;
import org.kasource.kaevent.event.export.EventExporter;
import org.kasource.kaevent.event.register.DefaultEventRegister;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.listener.register.EventListenerRegister;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegister;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegisterImpl;

/**
 * @author wigforss
 *
 */
///CLOVER:OFF
public class FrameworkConfigurer {

    public void configure(DefaultEventDispatcher eventDispatcher, String scanPath) {
        EventConfigFactory eventFactory = new EventConfigFactory(new DefaultBeanResolver());
        EventRegister eventRegister = null;
        if(scanPath != null) {
            EventExporter exporter = new AnnotationEventExporter(scanPath, eventFactory);
            eventRegister = new DefaultEventRegister(exporter);
        } else {
            eventRegister = new DefaultEventRegister(null);
        }
        
        ChannelRegister channelRegister = new ChannelRegisterImpl();
      
        SourceObjectListenerRegister soListenerRegister = new SourceObjectListenerRegisterImpl(eventRegister);
        EventMethodInvoker invoker = new EventMethodInvoker(eventRegister);
        EventSender eventSender = new EventSender(channelRegister,(EventListenerRegister) soListenerRegister,invoker);
        ChannelFactory channelFactory = new ChannelFactory(channelRegister, eventRegister, invoker);
        eventDispatcher.setChannelRegister(channelRegister);
        eventDispatcher.setSourceObjectListenerRegister(soListenerRegister);
        eventDispatcher.setEventQueue(new ThreadPoolQueueExecutor(eventSender));
        eventDispatcher.setEventSender(eventSender);
        eventDispatcher.setChannelFactory(channelFactory);
    }
}
