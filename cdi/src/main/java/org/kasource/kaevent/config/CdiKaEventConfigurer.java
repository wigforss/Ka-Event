package org.kasource.kaevent.config;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.cdi.EventScanPackage;
import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.channel.ChannelRegisterImpl;
import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.event.config.EventBuilderFactory;
import org.kasource.kaevent.event.config.EventBuilderFactoryImpl;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.dispatch.DispatcherQueueThread;
import org.kasource.kaevent.event.dispatch.EventMethodInvoker;
import org.kasource.kaevent.event.dispatch.EventMethodInvokerImpl;
import org.kasource.kaevent.event.dispatch.EventQueueRegister;
import org.kasource.kaevent.event.dispatch.EventQueueRegisterImpl;
import org.kasource.kaevent.event.dispatch.EventRouter;
import org.kasource.kaevent.event.dispatch.EventRouterImpl;
import org.kasource.kaevent.event.dispatch.ThreadPoolQueueExecutor;
import org.kasource.kaevent.event.export.AnnotationEventExporter;
import org.kasource.kaevent.event.register.DefaultEventRegister;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegister;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegisterImpl;



@ApplicationScoped
public class CdiKaEventConfigurer  extends KaEventConfigurer {

	@Inject
	private KaEventConfiguration configuration;
	
	@Inject
	private EventBuilderFactory eventBuilderFactory;
	
	@Inject
	private EventRegister eventRegister;
	
	@Inject
    private ChannelFactory channelFactory;
	
	@Inject 
	private ChannelRegister channelRegister;
	
	@Inject
	private BeanManager beanManager;
	
	
	@Inject @EventScanPackage
	private Instance<String> scanPackeName;
	
	
	public void configure() {
	    String scanClasspath = getScanClasspath();
	    if(scanClasspath != null) {
	        importAndRegisterEvents(new AnnotationEventExporter(scanClasspath), eventBuilderFactory, eventRegister);
	    }
	    Set<EventConfig> events = findEvents();
	    for(EventConfig event : events) {
	        eventRegister.registerEvent(event);
	    }
	    createChannels();
	  
	    registerEventsAtChannels(eventRegister, channelFactory, channelRegister);
	   
	    configuration.toString();
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    private Set<EventConfig> findEvents() {
	    Set<EventConfig> events = new HashSet<EventConfig>();
	    Set<Bean<?>> beans = beanManager.getBeans(EventConfig.class);
	    for(Bean bean : beans) {
	       Object instance = beanManager.getContext(bean.getScope()).get(bean, beanManager.createCreationalContext(bean));
	       events.add((EventConfig) instance);
	    }
	    return events;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    private void createChannels() {
        Set<Bean<?>> beans = beanManager.getBeans(Channel.class);
        for(Bean bean : beans) {
            Object object = beanManager.getContext(bean.getScope()).get(bean, beanManager.createCreationalContext(bean));
            object.toString();
            
         }
	}
	
	
	private String getScanClasspath() {
	    if(!scanPackeName.isUnsatisfied()) {
            if(scanPackeName.isAmbiguous()) {
                throw new IllegalStateException("Injection point has ambiguous dependencies @EventScanPath CdiKaEventConfigurer.scanPackeName");
            } else {
                return scanPackeName.get();
            }
        } else {
            return null;
        }
	}
	
	
	/**
	 * Provides the Event Factory.
	 * 
	 * @param beanResolver Bean Resolver to use.
	 * 
	 * @return Event Factory to use.
	 **/
	@Produces @ApplicationScoped
	EventBuilderFactory provideEventFactory(BeanResolver beanResolver, EventQueueRegister eventQueueRegister) {
		return new EventBuilderFactoryImpl(beanResolver, eventQueueRegister);
	}

	/**
	 * Provides Event Register.
	 * 
	 * @param eventBuilderFactory Event Factory.
	 * 
	 * @return Event Register to use.
	 **/
	@Produces @ApplicationScoped
	EventRegister provideEventRegister(EventBuilderFactory eventBuilderFactory) {
		return new DefaultEventRegister(eventBuilderFactory);
	}

	/**
	 * Provides Event Method Invoker.
	 * 
	 * @param eventRegister Event Register.
	 * 
	 * @return  Event Method Invoker to use.
	 **/
	@Produces @ApplicationScoped
	EventMethodInvoker provideEventMethodInvoker(EventRegister eventRegister) {
		return new EventMethodInvokerImpl(eventRegister);
	}

	/**
	 * Provides Source Object Listener Register.
	 * 
	 * @param eventRegister Event Register.
	 * @param beanResolver  Bean Resolver.
	 * 
	 * @return Source Object Listener Register to use.
	 **/
	@Produces @ApplicationScoped
	SourceObjectListenerRegister provideSourceObjectListenerRegister(EventRegister eventRegister, 
	                                                                 BeanResolver beanResolver) {
		return new SourceObjectListenerRegisterImpl(eventRegister, beanResolver);
	}

	/**
	 * Provides Channel Register.
	 * 
	 * @return Channel Register to use.
	 **/
	@Produces @ApplicationScoped
	ChannelRegister provideChannelRegister() {
		return new ChannelRegisterImpl();
	}
	
	@Produces @ApplicationScoped
	EventQueueRegister provideEventQueueRegister() {
	    return new EventQueueRegisterImpl();
	}

	/**
	 * Provides Event Router.
	 * 
	 * @param channelRegister               Channel Register.
	 * @param sourceObjectListenerRegister  Source Object Listener Register.
	 * @param eventMethodInvoker            Event Method Invoker.
	 * 
	 * @return Event Router to use.
	 **/
	@Produces @ApplicationScoped
	EventRouter provideEventRouter(ChannelRegister channelRegister, 
	                               SourceObjectListenerRegister sourceObjectListenerRegister, 
	                               EventMethodInvoker eventMethodInvoker) {
		return new EventRouterImpl(channelRegister, sourceObjectListenerRegister, eventMethodInvoker);
	}

	/**
	 * Provides the Event Queue.
	 * 
	 * @param eventRouter Event Router.
	 * 
	 * @return Event Queue to use.
	 */
	@Produces @ApplicationScoped
	DispatcherQueueThread provideQueueThread(EventRouter eventRouter) {
	    DispatcherQueueThread eventQueue = new ThreadPoolQueueExecutor();
	    eventQueue.setEventRouter(eventRouter);
		return eventQueue;
	}

	/**
	 * Provides the Configuration object.
	 * 
	 * @param beanResolver     Bean Resolver
	 * @param channelFactory   Channel Factory
	 * @param channelRegister  Channel Register.
	 * @param eventDispatcher  Event Dispatcher.
	 * @param eventBuilderFactory     Event Factory
	 * @param eventMethodInvoker Event Method Invoker.
	 * @param eventRegister    Event Register.
	 * @param eventRouter      Event Router.
	 * @param queueThread      Event Queue.
	 * @param sourceObjectListenerRegister Source Object Listener Register
	 * 
	 * @return The result of configuring Ka-Event.
	 **/
	//CHECKSTYLE:OFF
	// Motivation: Number of parameters
	@Produces @ApplicationScoped
	KaEventConfiguration provideKaEventConfiguration(BeanResolver beanResolver, 
	                                ChannelFactory channelFactory, 
									ChannelRegister channelRegister,
									EventQueueRegister eventQueueRegister,
									EventDispatcher eventDispatcher,
									EventBuilderFactory eventBuilderFactory,
									EventMethodInvoker eventMethodInvoker,
									EventRegister eventRegister,
									EventRouter eventRouter,
									DispatcherQueueThread queueThread,
									SourceObjectListenerRegister sourceObjectListenerRegister) {
		KaEventConfigurationImpl configuration =  new KaEventConfigurationImpl();
		configuration.setBeanResolver(beanResolver);
		configuration.setChannelFactory(channelFactory);
		configuration.setChannelRegister(channelRegister);
		configuration.setEventDispatcher(eventDispatcher);
		configuration.setEventBuilderFactory(eventBuilderFactory);
		configuration.setEventMethodInvoker(eventMethodInvoker);
		configuration.setEventRegister(eventRegister);
		configuration.setEventRouter(eventRouter);
		configuration.setDefaultEventQueue(queueThread);
		configuration.setSourceObjectListenerRegister(sourceObjectListenerRegister);
		configuration.setEventQueueRegister(eventQueueRegister);
		KaEventInitializer.setConfiguration(configuration);
		return configuration;
	}
	
}
