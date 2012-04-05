package org.kasource.kaevent.config;

import org.kasource.kaevent.annotations.listener.RegisterListener;
import org.kasource.kaevent.annotations.listener.UnregisterListener;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.bean.GuiceBeanResolver;
import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.channel.ChannelRegisterImpl;
import org.kasource.kaevent.channel.GuiceChannelFactory;
import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.event.config.EventBuilderFactory;
import org.kasource.kaevent.event.config.EventBuilderFactoryImpl;
import org.kasource.kaevent.event.dispatch.DispatcherQueueThread;
import org.kasource.kaevent.event.dispatch.EventMethodInvoker;
import org.kasource.kaevent.event.dispatch.EventMethodInvokerImpl;
import org.kasource.kaevent.event.dispatch.EventRouter;
import org.kasource.kaevent.event.dispatch.EventRouterImpl;
import org.kasource.kaevent.event.dispatch.GuiceEventDispatcher;
import org.kasource.kaevent.event.dispatch.ThreadPoolQueueExecutor;
import org.kasource.kaevent.event.register.DefaultEventRegister;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.listener.register.RegisterListenerInterceptor;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegister;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegisterImpl;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Named;

/**
 * Ka-Event Configuration Module.
 * 
 * Extend this module to configure the Ka-Event Environment.
 * 
 * @author rikardwi
 **/
public class KaEventModule extends AbstractModule {

	private String scanClassPath;
	
	/**
	 * Configure.
	 **/
	@Override
	protected void configure() {
		bind(ChannelFactory.class).to(GuiceChannelFactory.class);
		bind(EventDispatcher.class).to(GuiceEventDispatcher.class);
		bind(BeanResolver.class).to(GuiceBeanResolver.class);	
		bindInterceptor(Matchers.any(), Matchers.annotatedWith(RegisterListener.class), 
			        new RegisterListenerInterceptor(true));
		bindInterceptor(Matchers.any(), Matchers.annotatedWith(UnregisterListener.class), 
		        new RegisterListenerInterceptor(false));

	}

	
	/**
	 * Provides the classpath (package name) to scan for @Event annotated classes.
	 *  
	 * @return The path to scan for events.
	 **/
	@Provides @Named("kaEvent.scan.package")
	String provideScanClassPath() {
		return scanClassPath;
	}

	/**
	 * Provides the Event Factory.
	 * 
	 * @param beanResolver Bean Resolver to use.
	 * 
	 * @return Event Factory to use.
	 **/
	@Provides @Singleton
	EventBuilderFactory provideEventFactory(BeanResolver beanResolver) {
		return new EventBuilderFactoryImpl(beanResolver);
	}

	/**
	 * Provides Event Register.
	 * 
	 * @param eventBuilderFactory Event Factory.
	 * 
	 * @return Event Register to use.
	 **/
	@Provides @Singleton
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
	@Provides @Singleton
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
	@Provides @Singleton
	SourceObjectListenerRegister provideSourceObjectListenerRegister(EventRegister eventRegister, 
	                                                                 BeanResolver beanResolver) {
		return new SourceObjectListenerRegisterImpl(eventRegister, beanResolver);
	}

	/**
	 * Provides Channel Register.
	 * 
	 * @return Channel Register to use.
	 **/
	@Provides @Singleton
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
	@Provides @Singleton
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
	@Provides @Singleton
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
	@Provides @Singleton
	KaEventConfiguration provideKaEventConfiguration(BeanResolver beanResolver, 
	                                ChannelFactory channelFactory, 
									ChannelRegister channelRegister,
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
		configuration.setQueueThread(queueThread);
		configuration.setSourceObjectListenerRegister(sourceObjectListenerRegister);
		
		KaEventInitializer.setConfiguration(configuration);
		return configuration;
	}
	//CHECKSTYLE:ON

	/**
	 * Set the classpath (package name) to scan for @Event annotated classes.
	 * <p/>
	 * scanClassPath may be a comma separated list of package names.
	 * 
	 * Note: All sub packages for the provied packages will also be scanned.
	 * 
	 * 
	 * @param scanClassPath package to scan for @Event annotated classes.
	 */
	protected void setScanClassPath(String scanClassPath) {
		this.scanClassPath = scanClassPath;
	}
	
	

}
