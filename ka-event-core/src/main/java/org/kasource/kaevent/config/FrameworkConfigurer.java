/**
 * 
 */
package org.kasource.kaevent.config;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.kasource.commons.util.ReflectionUtils;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.bean.DefaultBeanResolver;
import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.channel.ChannelRegisterImpl;
import org.kasource.kaevent.event.config.EventConfigFactory;
import org.kasource.kaevent.event.dispatch.DefaultEventDispatcher;
import org.kasource.kaevent.event.dispatch.DispatcherQueueThread;
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
public class FrameworkConfigurer {

    private Properties props;
    
    public void configure(DefaultEventDispatcher eventDispatcher) {
        configure(eventDispatcher, null);
    }
    
    public void configure(DefaultEventDispatcher eventDispatcher, String scanClassPath) {
        if(props == null) {
            props = getProperties();
        }
        // Scan path
        if(scanClassPath == null) {
            scanClassPath = props.getProperty("kaevent.scanClassPath");
        }
        
     
        // Bean Resolver
        String beanResolverClass = props.getProperty("kaevent.class.beanResolver", DefaultBeanResolver.class.getName());
        BeanResolver beanResolver = ReflectionUtils.getInstance(beanResolverClass, BeanResolver.class);
        
        
        EventConfigFactory eventFactory = new EventConfigFactory(beanResolver);
        
        // Event Exporter
        String eventExporterClass = props.getProperty("kaevent.class.eventExporter" , AnnotationEventExporter.class.getName()); 
        EventExporter exporter = ReflectionUtils.getInstance(eventExporterClass, EventExporter.class);
        
        // Event Register
        EventRegister eventRegister = null;
        if(scanClassPath != null) {
            eventRegister = new DefaultEventRegister(eventFactory,exporter, scanClassPath);
        } else {
            eventRegister = new DefaultEventRegister(eventFactory);
        }
        
        // Channel Register
        ChannelRegister channelRegister = new ChannelRegisterImpl();
      
        // Source Object Listener Register
        SourceObjectListenerRegister soListenerRegister = new SourceObjectListenerRegisterImpl(eventRegister);
        
        
        EventMethodInvoker invoker = new EventMethodInvoker(eventRegister);
        EventSender eventSender = new EventSender(channelRegister,(EventListenerRegister) soListenerRegister,invoker);
        
        // Channel Factory
        ChannelFactory channelFactory = new ChannelFactory(channelRegister, eventRegister, invoker);
        
        // DispatcherQueueThread
        String queueClass = props.getProperty("kaevent.class.queueThread", ThreadPoolQueueExecutor.class.getName());
        DispatcherQueueThread queueThread = ReflectionUtils.getInstance(queueClass, DispatcherQueueThread.class, eventSender);
        if(queueThread instanceof ThreadPoolQueueExecutor) {
            configureThreadPoolQueue((ThreadPoolQueueExecutor) queueThread);
        }
        eventDispatcher.setChannelRegister(channelRegister);
        eventDispatcher.setSourceObjectListenerRegister(soListenerRegister);
        eventDispatcher.setEventQueue(queueThread);
        eventDispatcher.setEventSender(eventSender);
        eventDispatcher.setChannelFactory(channelFactory);
        
    }

    
    private void configureThreadPoolQueue(ThreadPoolQueueExecutor queue) {
        String maxThreads = props.getProperty("kaevent.threadPoolQueueExecutor.maximumPoolSize");
        if(maxThreads != null) {
            queue.setMaximumPoolSize(Integer.parseInt(maxThreads));
        }
        String corePoolSize = props.getProperty("kaevent.threadPoolQueueExecutor.corePoolSize");
        if(corePoolSize != null) {
            queue.setCorePoolSize(Integer.parseInt(corePoolSize));
        }
        String keepAliveTime = props.getProperty("kaevent.threadPoolQueueExecutor.keepAliveTime");
        if(keepAliveTime != null) {
            queue.setKeepAliveTime(Integer.parseInt(keepAliveTime), TimeUnit.MILLISECONDS);
        }
    }
    
    

    private Properties getProperties() {
        Properties props = new Properties();
        try {
            InputStream propStream = FrameworkConfigurer.class.getClassLoader().getResourceAsStream(
                    "kaevent.properties");
            if (propStream != null) {
                props.load(propStream);
                propStream.close();
            }
        } catch (IOException e) {
        }
        return props;
    }
}
