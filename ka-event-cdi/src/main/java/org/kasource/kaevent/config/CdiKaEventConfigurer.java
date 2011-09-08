package org.kasource.kaevent.config;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.UnsatisfiedResolutionException;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.weld.exceptions.DeploymentException;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.cdi.EventScanPackage;
import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.channel.ChannelRegisterImpl;
import org.kasource.kaevent.channel.NoSuchChannelException;
import org.kasource.kaevent.config.KaEventConfiguration;
import org.kasource.kaevent.config.KaEventConfigurationImpl;
import org.kasource.kaevent.config.KaEventInitializer;
import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.config.EventFactory;
import org.kasource.kaevent.event.config.EventFactoryImpl;
import org.kasource.kaevent.event.dispatch.DispatcherQueueThread;
import org.kasource.kaevent.event.dispatch.EventMethodInvoker;
import org.kasource.kaevent.event.dispatch.EventMethodInvokerImpl;
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
	private EventFactory eventFactory;
	
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
	        importAndRegisterEvents(new AnnotationEventExporter(scanClasspath), eventFactory, eventRegister);
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
	EventFactory provideEventFactory(BeanResolver beanResolver) {
		return new EventFactoryImpl(beanResolver);
	}

	/**
	 * Provides Event Register.
	 * 
	 * @param eventFactory Event Factory.
	 * 
	 * @return Event Register to use.
	 **/
	@Produces @ApplicationScoped
	EventRegister provideEventRegister(EventFactory eventFactory) {
		return new DefaultEventRegister(eventFactory);
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
		return new ThreadPoolQueueExecutor(eventRouter);
	}

	/**
	 * Provides the Configuration object.
	 * 
	 * @param beanResolver     Bean Resolver
	 * @param channelFactory   Channel Factory
	 * @param channelRegister  Channel Register.
	 * @param eventDispatcher  Event Dispatcher.
	 * @param eventFactory     Event Factory
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
									EventDispatcher eventDispatcher,
									EventFactory eventFactory,
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
		configuration.setEventFactory(eventFactory);
		configuration.setEventMethodInvoker(eventMethodInvoker);
		configuration.setEventRegister(eventRegister);
		configuration.setEventRouter(eventRouter);
		configuration.setQueueThread(queueThread);
		configuration.setSourceObjectListenerRegister(sourceObjectListenerRegister);
		System.out.println("Initialized");
		KaEventInitializer.setConfiguration(configuration);
		return configuration;
	}
	
}
