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
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.kasource.commons.util.ReflectionUtils;
import org.kasource.commons.util.StringUtils;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.bean.DefaultBeanResolver;
import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.channel.ChannelFactoryImpl;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.channel.ChannelRegisterImpl;
import org.kasource.kaevent.channel.NoSuchChannelException;
import org.kasource.kaevent.event.Event;
import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.config.EventFactory;
import org.kasource.kaevent.event.config.EventFactoryImpl;
import org.kasource.kaevent.event.dispatch.DispatcherQueueThread;
import org.kasource.kaevent.event.dispatch.EventMethodInvokerImpl;
import org.kasource.kaevent.event.dispatch.EventSender;
import org.kasource.kaevent.event.dispatch.EventSenderImpl;
import org.kasource.kaevent.event.dispatch.ThreadPoolQueueExecutor;
import org.kasource.kaevent.event.export.AnnotationEventExporter;
import org.kasource.kaevent.event.export.EventExporter;
import org.kasource.kaevent.event.export.XmlConfigEventExporter;
import org.kasource.kaevent.event.register.DefaultEventRegister;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.listener.register.EventListenerRegister;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegisterImpl;

/**
 * @author wigforss
 * 
 */
public class KaEventConfigurer  {
    private static Logger logger = Logger.getLogger(KaEventConfigurer.class);
    
    
    
   
    
   
    
    private KaEventConfigurationImpl configuration = null;
    
    
    
    
    public void configure(EventDispatcher eventDispatcher,String configLocation) {
        
        KaEventConfig xmlConfig = null;
        if(configLocation != null) {
            xmlConfig = loadXmlFromPath(configLocation);
        } else {
            // Try default location
            try {
              xmlConfig = loadXmlFromPath("classpath:kaevent-config.xml");
            } catch(IllegalArgumentException iae) {} // Ignore
        }
        if(xmlConfig != null) {
            configuration = configureByXml(xmlConfig);
        } else {
            configuration = defaultConfiguration();
        }
        
        configuration.setEventDispatcher(eventDispatcher);
        KaEventInitializer.setConfiguration(configuration);
        
    }
    
    
    
    
    private KaEventConfigurationImpl defaultConfiguration( ) {
        KaEventConfigurationImpl config = new KaEventConfigurationImpl();
        // Bean resolver
        config.setBeanResolver(new DefaultBeanResolver());
      
        // Events
        config.setEventFactory( new EventFactoryImpl(config.getBeanResolver()));
        
        config.setEventRegister(new DefaultEventRegister(config.getEventFactory()));
     
        // Channel Register
        config.setChannelRegister(new ChannelRegisterImpl());
       
      
        // Source Object Listener Register
        config.setSourceObjectListenerRegister(new SourceObjectListenerRegisterImpl(config.getEventRegister(), config.getBeanResolver()));
        
        config.setEventMethodInvoker(new EventMethodInvokerImpl(config.getEventRegister()));
        
        config.setEventSender(new EventSenderImpl(config.getChannelRegister(), config.getSourceObjectListenerRegister(),config.getEventMethodInvoker()));
        
        // Channel Factory
        config.setChannelFactory(new ChannelFactoryImpl(config.getChannelRegister(), config.getEventRegister(),config.getEventMethodInvoker(), config.getBeanResolver()));
        
        config.setQueueThread(new ThreadPoolQueueExecutor(config.getEventSender()));
       
        return config;
        
       
    }
    
    private KaEventConfigurationImpl configureByXml(KaEventConfig xmlConfig) {
        KaEventConfigurationImpl config = new KaEventConfigurationImpl();
        // Bean Resolver
        BeanResolver beanResolver = null;
        KaEventConfig.BeanResolver beanResolverConfig = xmlConfig.getBeanResolver();
        if(beanResolverConfig != null && beanResolverConfig.getClazz() != null) {
            config.setBeanResolver(ReflectionUtils.getInstance(beanResolverConfig.getClazz(), BeanResolver.class));
        } else {
            config.setBeanResolver( new DefaultBeanResolver());
        }
        
        // Events
        config.setEventFactory( new EventFactoryImpl(config.getBeanResolver()));
        
        config.setEventRegister(new DefaultEventRegister(config.getEventFactory()));
     
        config.setEventMethodInvoker(new EventMethodInvokerImpl(config.getEventRegister()));
     
        config.setSourceObjectListenerRegister(new SourceObjectListenerRegisterImpl(config.getEventRegister(), config.getBeanResolver()));
        
       
        
        // Channel Register
        config.setChannelRegister(new ChannelRegisterImpl());
        
        
        // Sender
        config.setEventSender(new EventSenderImpl(config.getChannelRegister(), config.getSourceObjectListenerRegister(),config.getEventMethodInvoker()));
        
        // Channel Factory
        config.setChannelFactory(new ChannelFactoryImpl(config.getChannelRegister(), config.getEventRegister(),config.getEventMethodInvoker(), config.getBeanResolver()));
        
        // Queue Thread
        if(xmlConfig.getQueueThread() == null) {
            config.setQueueThread(new ThreadPoolQueueExecutor(config.getEventSender()));
            ThreadPoolQueueExecutor threadPoolExecutor = new ThreadPoolQueueExecutor(config.getEventSender());
            if(xmlConfig.getThreadPoolExecutor() != null) {
                if(xmlConfig.getThreadPoolExecutor().getMaximumPoolSize() != null) {
                   threadPoolExecutor.setMaximumPoolSize(xmlConfig.getThreadPoolExecutor().getMaximumPoolSize());
                }
                if(xmlConfig.getThreadPoolExecutor().getCorePoolSize() != null) {
                    threadPoolExecutor.setCorePoolSize(xmlConfig.getThreadPoolExecutor().getCorePoolSize());
                 }
                if(xmlConfig.getThreadPoolExecutor().getKeepAliveTime() != null) {
                    threadPoolExecutor.setKeepAliveTime(xmlConfig.getThreadPoolExecutor().getKeepAliveTime().longValue(), TimeUnit.MILLISECONDS);
                 }               
            }
            config.setQueueThread(threadPoolExecutor);
        } else {
        	try {
        		config.setQueueThread(ReflectionUtils.getInstance(xmlConfig.getQueueThread().getClazz(), DispatcherQueueThread.class, new Class<?>[]{EventSender.class},new Object[]{config.getEventSender()}));
        	} catch(IllegalStateException ise) {
        		config.setQueueThread(ReflectionUtils.getInstance(xmlConfig.getQueueThread().getClazz(), DispatcherQueueThread.class));
        	}
            
        }
       
       
        // import events
        KaEventConfig.Events events = xmlConfig.getEvents();
        if(events != null) {
            String scanPath = events.getScanClassPath();
            if(scanPath != null && scanPath.length() > 0) {
                importAndRegisterEvents(new AnnotationEventExporter(scanPath),config.getEventFactory(), config.getEventRegister());
            }
            importAndRegisterEvents(new XmlConfigEventExporter( events.getEvent(), beanResolver),config.getEventFactory(),config.getEventRegister());
        }
        
        createChannels(xmlConfig, config);
        
        registerEventsAtChannels(config);
        
        return config;
     
        
    }

    /**
     *  Register @Event annotated events with the channels referenced in the channels attribute
     */
    protected void registerEventsAtChannels(KaEventConfiguration config) {
        EventRegister eventRegister = config.getEventRegister();
        ChannelFactory channelFactory = config.getChannelFactory();
        ChannelRegister channelRegister = config.getChannelRegister();
        for(Class<? extends EventObject> eventClass : eventRegister.getEventClasses()) {
            Event eventAnno = eventClass.getAnnotation(Event.class);
            if(eventAnno != null && eventAnno.channels().length > 0) {
                String[] channelsByEvent = eventAnno.channels();
                for(String channelName : channelsByEvent) {
                    Channel channel = null;
                    try {
                        channel = channelRegister.getChannel(channelName);
                        channel.registerEvent(eventClass);
                    } catch(NoSuchChannelException nsce) { 
                        if(eventAnno.createChannels()) {
                            channel = channelFactory.createChannel(channelName);
                        } else {
                            throw new NoSuchChannelException(eventClass.getName() +" @Event annotation referenced to a channel named "+channelName+" which does not exist!");
                        }
                    }
                    channel.registerEvent(eventClass);
                }
            }
        }
    }

    /**
     * 
     */
    private void createChannels(KaEventConfig xmlConfig,KaEventConfiguration config) {
        EventRegister eventRegister = config.getEventRegister();
        ChannelFactory channelFactory = config.getChannelFactory();
        // Create channels
        KaEventConfig.Channels channels = xmlConfig.getChannels();
        if(channels != null) {
            for(KaEventConfig.Channels.Channel channel : channels.getChannel()) {
                String name = channel.getName();
                Set<Class <? extends EventObject>> eventSet = new HashSet<Class <? extends EventObject>>();
                if(channel.getHandle() != null) {
                for(KaEventConfig.Channels.Channel.Handle handleEvent : channel.getHandle()) {
                    String eventName = ((KaEventConfig.Events.Event) handleEvent.getEvent()).getName();
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
    
    protected void  importAndRegisterEvents(EventExporter eventExporter,EventFactory eventFactory, EventRegister eventRegister) {
        Set<EventConfig> events;
        try {
            events = eventExporter.exportEvents(eventFactory);
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


    
      

    private KaEventConfig loadXmlFromPath(String inPath) {
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
                xmlStream = KaEventConfigurer.class.getClassLoader().getResourceAsStream(path);
                if(xmlStream == null) {
                    throw new IllegalArgumentException("Could not load xml configuration file "+inPath+" from class path");
                }
            } else {
                xmlStream = new FileInputStream(path);
            }
            KaEventConfig xmlConfig = loadXmlConfig(xmlStream);
            if(xmlConfig == null) {
                throw new IllegalArgumentException("Could not unmarshal xml configuration file "+inPath);
            }
            return xmlConfig;
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
         Object object = unmarshaller.unmarshal(istrm);
        
         return (KaEventConfig) object;
      
    }
    

 
    
   

}