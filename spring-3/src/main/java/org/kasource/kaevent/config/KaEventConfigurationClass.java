package org.kasource.kaevent.config;

import java.util.Arrays;

import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.bean.SpringBeanResolver;
import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.channel.ChannelRegisterImpl;
import org.kasource.kaevent.channel.SpringChannelFactory;
import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.event.config.EventBuilderFactory;
import org.kasource.kaevent.event.config.EventBuilderFactoryImpl;
import org.kasource.kaevent.event.dispatch.DispatcherQueueThread;
import org.kasource.kaevent.event.dispatch.EventMethodInvoker;
import org.kasource.kaevent.event.dispatch.EventMethodInvokerImpl;
import org.kasource.kaevent.event.dispatch.EventQueueRegister;
import org.kasource.kaevent.event.dispatch.EventQueueRegisterImpl;
import org.kasource.kaevent.event.dispatch.EventRouter;
import org.kasource.kaevent.event.dispatch.EventRouterImpl;
import org.kasource.kaevent.event.dispatch.SpringEventDispatcher;
import org.kasource.kaevent.event.dispatch.ThreadPoolQueueExecutor;
import org.kasource.kaevent.event.register.DefaultEventRegister;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.listener.register.RegisterListenerBeanPostProcessor;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegister;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegisterImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration to be used with AnnotationConfigApplicationContext
 * or AnnotationConfigWebApplicationContext.
 * 
 * 
 * @author rikardwi
 **/
@Configuration
public class KaEventConfigurationClass {

    private static String scanClassPath;

    
    /**
     * Scan for events annotated with @Event
     * 
     * @param basePackage base package to scan for events.
     **/
    public static void scan(String... basePackage) {
        if(basePackage.length > 0) {
            scanClassPath = Arrays.toString(basePackage);
            // Remove [ ] and spaces
            scanClassPath = scanClassPath.substring(1, scanClassPath.length()-1).replace(" ","");
        }
        
        
        
    }
    
    
    @Bean(name = "kaEvent.beanResolver")
    public BeanResolver provideBeanResolver() {
        return new SpringBeanResolver();
    }
    
    @Bean(name = "kaEvent.eventQueueRegister")
    public EventQueueRegister provideEventQueueRegister() {
        return new EventQueueRegisterImpl();
    }
    
    @Bean(name = "kaEvent.postBeanProcessor")
    RegisterListenerBeanPostProcessor provideRegisterListenerBeanPostProcessor() {
        return new RegisterListenerBeanPostProcessor();
    }
    
    
    @Bean(name = "kaEvent.channelRegister") 
    public ChannelRegister provideChannelRegister() {
        return new ChannelRegisterImpl();
    }
    
    @Bean(name = "kaEvent.eventBuilderFactory")
    @Autowired
    public EventBuilderFactory provideEventBuilderFactory(BeanResolver beanResolver, EventQueueRegister eventQueueRegister) {
        return new EventBuilderFactoryImpl(beanResolver, eventQueueRegister);
    }
    
    @Bean(name = "kaEvent.eventRegister")
    @Autowired
    public EventRegister provideEventRegister(EventBuilderFactory eventBuilderFactory) {
        return new DefaultEventRegister(eventBuilderFactory);
    }
    
   
    @Bean(name = "kaEvent.sourceObjectListenerRegister")
    @Autowired
    public SourceObjectListenerRegister provideSourceObjectListenerRegister(BeanResolver beanResolver, EventRegister eventRegister) {
        return new SourceObjectListenerRegisterImpl(eventRegister, beanResolver);
    }
    
    @Bean(name = "kaEvent.eventMethodInvoker")
    @Autowired
    public EventMethodInvoker provideEventMethodInvoker(EventRegister eventRegister) {
        return new EventMethodInvokerImpl(eventRegister);
    }
    
    @Bean(name = "kaEvent.eventRouter")
    @Autowired
    public EventRouter provideEventRouter(ChannelRegister channelRegister, SourceObjectListenerRegister sourceObjectListenerRegister, EventMethodInvoker eventMethodInvoker) {
        return new EventRouterImpl(channelRegister, sourceObjectListenerRegister, eventMethodInvoker);
    }
    
    @Bean(name = "kaEvent.channelFactory")
    @Autowired
    public ChannelFactory provideChannelFactory(ChannelRegister channelRegister,
                                                EventRegister eventRegister,
                                                EventMethodInvoker eventMethodInvoker,
                                                BeanResolver beanResolver) {
        return new SpringChannelFactory(channelRegister,
                    eventRegister,
                    eventMethodInvoker,
                    beanResolver);
    }
    
    @Bean(name = "kaEvent.eventQueue")
    @Autowired
    public DispatcherQueueThread provideDispatcherQueueThread(EventRouter eventRouter) {
        ThreadPoolQueueExecutor eventQueue =  new ThreadPoolQueueExecutor();
        eventQueue.setEventRouter(eventRouter);
        return eventQueue;
    }
    
    @Bean(name = "kaEvent.eventDispatcher")
    @Autowired
    public EventDispatcher provideEventDispatcher(ChannelRegister channelRegister,
                ChannelFactory channelFactory,
                SourceObjectListenerRegister sourceObjectListenerRegister,
                DispatcherQueueThread eventQueue,
                EventRouter eventRouter,
                EventRegister eventRegister) {
        return new SpringEventDispatcher(channelRegister,
                     channelFactory,
                     sourceObjectListenerRegister,
                     eventQueue,
                     eventRouter,
                     eventRegister);
    }
    
    @Bean(name = "kaEvent.configuration")
    @Autowired
    public KaEventConfiguration provideKaEventConfiguration(ChannelRegister channelRegister,
                ChannelFactory channelFactory,
                SourceObjectListenerRegister sourceObjectListenerRegister,
                DispatcherQueueThread eventQueue,
                EventRouter eventRouter,
                EventRegister eventRegister,
                EventDispatcher eventDispatcher,
                BeanResolver beanResolver,
                EventBuilderFactory eventBuilderFactory,
                EventMethodInvoker eventMethodInvoker,
                EventQueueRegister eventQueueRegister) {
        KaEventConfigurationImpl config = new KaEventConfigurationImpl();
        config.setChannelRegister(channelRegister);
        config.setChannelFactory(channelFactory);
        config.setSourceObjectListenerRegister(sourceObjectListenerRegister);
        config.setDefaultEventQueue(eventQueue);
        config.setEventRouter(eventRouter);
        config.setEventRegister(eventRegister);
        config.setEventDispatcher(eventDispatcher);
        config.setBeanResolver(beanResolver);
        config.setEventBuilderFactory(eventBuilderFactory);
        config.setEventMethodInvoker(eventMethodInvoker);
        config.setEventQueueRegister(eventQueueRegister);
        return config;
    }
    
    @Bean(name = "kaEvent.configurer", initMethod="configure")
    @Autowired
    public SpringKaEventConfigurer provideConfigurer(KaEventConfiguration configuration) {
        SpringKaEventConfigurer configurer = new SpringKaEventConfigurer(configuration);
        configurer.setScanClassPath(scanClassPath);
        return configurer;
    }
    
}
