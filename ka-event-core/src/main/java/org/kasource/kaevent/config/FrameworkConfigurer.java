/**
 * 
 */
package org.kasource.kaevent.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.util.EventObject;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.kasource.commons.util.ReflectionUtils;
import org.kasource.commons.util.StringUtils;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.bean.DefaultBeanResolver;
import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.channel.ChannelRegisterImpl;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.config.EventConfigFactory;
import org.kasource.kaevent.event.dispatch.DefaultEventDispatcher;
import org.kasource.kaevent.event.dispatch.DispatcherQueueThread;
import org.kasource.kaevent.event.dispatch.EventMethodInvoker;
import org.kasource.kaevent.event.dispatch.EventSender;
import org.kasource.kaevent.event.dispatch.ThreadPoolQueueExecutor;
import org.kasource.kaevent.event.export.AnnotationEventExporter;
import org.kasource.kaevent.event.export.EventExporter;
import org.kasource.kaevent.event.export.XmlConfigEventExporter;
import org.kasource.kaevent.event.register.DefaultEventRegister;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.listener.register.EventListenerRegister;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegister;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegisterImpl;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * @author wigforss
 * 
 */
public class FrameworkConfigurer {
    private static Logger logger = Logger.getLogger(FrameworkConfigurer.class);
    private  KaEventConfig config;
    
    public void configure(DefaultEventDispatcher eventDispatcher, String configLocation) {
        KaEventConfig config = null;
        if(configLocation != null) {
            loadXmlFromPath(configLocation);
        } else {
            // Try default location
            try {
              loadXmlFromPath("classpath:kaevent-config.xml");
            } catch(IllegalArgumentException iae) {} // Ignore
        }
        if(config != null) {
            configureByXml(eventDispatcher);
        } else {
            defaultConfiguration(eventDispatcher);
        }
    }
    
    private void defaultConfiguration(DefaultEventDispatcher eventDispatcher) {
        // Bean resolver
        BeanResolver beanResolver  = new DefaultBeanResolver();
        // Events
        EventConfigFactory eventFactory = new EventConfigFactory(beanResolver);
        EventRegister eventRegister = new DefaultEventRegister(eventFactory);
        // Channel Register
        ChannelRegister channelRegister = new ChannelRegisterImpl();
      
        // Source Object Listener Register
        SourceObjectListenerRegister soListenerRegister = new SourceObjectListenerRegisterImpl(eventRegister);
        EventMethodInvoker invoker = new EventMethodInvoker(eventRegister);
        EventSender eventSender = new EventSender(channelRegister,(EventListenerRegister) soListenerRegister,invoker);
        
        // Channel Factory
        ChannelFactory channelFactory = new ChannelFactory(channelRegister, eventRegister, invoker);
        DispatcherQueueThread queueThread = new ThreadPoolQueueExecutor(eventSender);
        eventDispatcher.setChannelRegister(channelRegister);
        eventDispatcher.setSourceObjectListenerRegister(soListenerRegister);
        eventDispatcher.setEventQueue(queueThread);
        eventDispatcher.setEventSender(eventSender);
        eventDispatcher.setChannelFactory(channelFactory);
    }
    
    private void configureByXml(DefaultEventDispatcher eventDispatcher) {
        // Bean Resolver
        BeanResolver beanResolver = null;
        KaEventConfig.BeanResolver beanResolverConfig = config.getBeanResolver();
        if(beanResolverConfig != null && beanResolverConfig.getClazz() != null) {
            beanResolver = ReflectionUtils.getInstance(beanResolverConfig.getClazz(), BeanResolver.class);
        } else {
            beanResolver = new DefaultBeanResolver();
        }
        // Events
        EventConfigFactory eventFactory = new EventConfigFactory(beanResolver);
        EventRegister eventRegister = new DefaultEventRegister(eventFactory);
        EventMethodInvoker invoker = new EventMethodInvoker(eventRegister);
        SourceObjectListenerRegister soListenerRegister = new SourceObjectListenerRegisterImpl(eventRegister);
        
        // Channel Register
        ChannelRegister channelRegister = new ChannelRegisterImpl();
        
        // Sender
        EventSender eventSender = new EventSender(channelRegister,(EventListenerRegister) soListenerRegister,invoker);
        
        // Channel Factory
        ChannelFactory channelFactory = new ChannelFactory(channelRegister, eventRegister, invoker);
        
        // Queue Thread
        DispatcherQueueThread queueThread = null;
        if(config.getQueueThread() == null) {
            ThreadPoolQueueExecutor threadPoolExecutor = new ThreadPoolQueueExecutor(eventSender);
            if(config.getThreadPoolExecutor() != null) {
                if(config.getThreadPoolExecutor().getMaximumPoolSize() != null) {
                   threadPoolExecutor.setMaximumPoolSize(config.getThreadPoolExecutor().getMaximumPoolSize());
                }
                if(config.getThreadPoolExecutor().getCorePoolSize() != null) {
                    threadPoolExecutor.setCorePoolSize(config.getThreadPoolExecutor().getCorePoolSize());
                 }
                if(config.getThreadPoolExecutor().getKeepAliveTime() != null) {
                    threadPoolExecutor.setKeepAliveTime(config.getThreadPoolExecutor().getKeepAliveTime().longValue(), TimeUnit.MILLISECONDS);
                 }
                queueThread = threadPoolExecutor;
            }
        } else {
            queueThread = ReflectionUtils.getInstance(config.getQueueThread().getClazz(), DispatcherQueueThread.class, eventSender);
            
        }
        
        
      
        
        
        eventDispatcher.setChannelRegister(channelRegister);
        eventDispatcher.setSourceObjectListenerRegister(soListenerRegister);
        eventDispatcher.setEventQueue(queueThread);
        eventDispatcher.setEventSender(eventSender);
        eventDispatcher.setChannelFactory(channelFactory);
       
       
        // import events
        KaEventConfig.Events events = config.getEvents();
        if(events != null) {
            String scanPath = events.getScanClassPath();
            if(scanPath != null && scanPath.length() > 0) {
                importAndRegisterEvents(new AnnotationEventExporter(scanPath),eventFactory,eventRegister);
            }
            importAndRegisterEvents(new XmlConfigEventExporter( events.getEvent(), beanResolver),eventFactory,eventRegister);
        }
        
        // Create channels
        KaEventConfig.Channels channels = config.getChannels();
        if(channels != null) {
            for(KaEventConfig.Channels.Channel channel : channels.getChannel()) {
                String name = channel.getName();
                Set<Class <? extends EventObject>> eventSet = new HashSet<Class <? extends EventObject>>();
                if(channel.getHandle() != null) {
                for(KaEventConfig.Channels.Channel.Handle handleEvent : channel.getHandle()) {
                    String eventName = handleEvent.getEvent().toString();
                    EventConfig eventConfig = eventRegister.getEventByName(eventName);
                    if(eventConfig != null) {
                        eventSet.add(eventConfig.getEventClass());
                    } else {
                        throw new IllegalStateException("Event "+eventName+" could not be found!");
                    }
                }
                }
                if(eventSet.isEmpty()) {
                    channelFactory.createChannel(name);
                } else {
                    channelFactory.createChannel(name, eventSet);
                }
                
            }
        }
     
        
    }
    
    private void  importAndRegisterEvents(EventExporter eventExporter,EventConfigFactory eventConfigFactory, EventRegister eventRegister) {
        Set<EventConfig> events;
        try {
            events = eventExporter.exportEvents(eventConfigFactory);
            for(EventConfig event : events) {
                eventRegister.registerEvent(event);
            }
        } catch (RuntimeException re) {
          logger.error("Error when importing annotated events",re);
          throw re;
        } catch (IOException e) {
            throw new IllegalStateException("Could not import events", e);
        } 
      
    }

/*
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
*/
    
      

    private void loadXmlFromPath(String inPath) {
        boolean fromFile = false;
        String path = inPath;
        if(path.startsWith("classpath:")) {
            path = path.substring("classpath:".length()); 
        } else if(path.startsWith("file:")) {
            path = path.substring("file:".length());
            fromFile = true;
        }
        path = StringUtils.replaceVariables(path, null, true);
        InputStream xmlStream = null;
       
        try {
            if(!fromFile) {
                xmlStream = FrameworkConfigurer.class.getClassLoader().getResourceAsStream(path);
                if(xmlStream == null) {
                    throw new IllegalArgumentException("Could not load xml configuration file "+inPath+" from class path");
                }
            } else {
                xmlStream = new FileInputStream(path);
            }
            config = loadXmlConfig(xmlStream);
        } catch (JAXBException e) {
           throw new IllegalArgumentException("Could not parse xml configuration file "+inPath,e);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Could not load xml configuration file "+inPath,e);
        } finally {
            if(xmlStream != null) {
                try {
                    xmlStream.close();
                } catch (IOException e) {} // Ignore
            }
        }
        
    }
    
    private KaEventConfig loadXmlConfig(InputStream istrm) throws JAXBException {
         JAXBContext jaxbContext = JAXBContext.newInstance(KaEventConfig.class.getPackage().getName());

         Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
         
         return (KaEventConfig) unmarshaller.unmarshal(istrm);
      
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
