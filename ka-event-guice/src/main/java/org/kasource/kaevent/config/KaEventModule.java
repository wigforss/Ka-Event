package org.kasource.kaevent.config;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

import org.kasource.kaevent.annotations.listener.ChannelListener;
import org.kasource.kaevent.annotations.listener.RegisterListener;
import org.kasource.kaevent.annotations.listener.UnregisterListener;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.bean.GuiceBeanResolver;
import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.channel.ChannelRegisterImpl;
import org.kasource.kaevent.channel.GuiceChannelFactory;
import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.event.config.EventFactory;
import org.kasource.kaevent.event.config.EventFactoryImpl;
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

public class KaEventModule extends AbstractModule {

	private String scanClassPath;
	
	
	
	public  KaEventModule() {	
	}
	
	
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

	
	
	@Provides @Named("kaEvent.scan.package")
	String provideScanClassPath() {
		return scanClassPath;
	}

	@Provides @Singleton
	EventFactory provideEventFactory(BeanResolver beanResolver) {
		return new EventFactoryImpl(beanResolver);
	}

	@Provides @Singleton
	EventRegister provideEventRegister(EventFactory eventFactory) {
		return new DefaultEventRegister(eventFactory);
	}

	@Provides @Singleton
	EventMethodInvoker provideEventMethodInvoker(EventRegister eventRegister) {
		return new EventMethodInvokerImpl(eventRegister);
	}

	@Provides @Singleton
	SourceObjectListenerRegister provideSourceObjectListenerRegister(EventRegister eventRegister, BeanResolver beanResolver) {
		return new SourceObjectListenerRegisterImpl(eventRegister, beanResolver);
	}

	@Provides @Singleton
	ChannelRegister provideChannelRegister() {
		return new ChannelRegisterImpl();
	}

	@Provides @Singleton
	EventRouter provideEventRouter(ChannelRegister channelRegister, SourceObjectListenerRegister sourceObjectListenerRegister, EventMethodInvoker eventMethodInvoker) {
		return new EventRouterImpl(channelRegister, sourceObjectListenerRegister, eventMethodInvoker);
	}

	
	@Provides @Singleton
	DispatcherQueueThread provideQueueThread(EventRouter eventRouter) {
		return new ThreadPoolQueueExecutor(eventRouter);
	}


	@Provides @Singleton
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


	/**
	 * @param scanClassPath the scanClassPath to set
	 */
	protected void setScanClassPath(String scanClassPath) {
		this.scanClassPath = scanClassPath;
	}
	
	

}
