package org.kasource.kaevent.config;

import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.bean.GuiceBeanResolver;
import org.kasource.kaevent.channel.ChannelFactory;
import org.kasource.kaevent.channel.ChannelFactoryImpl;
import org.kasource.kaevent.channel.ChannelRegister;
import org.kasource.kaevent.channel.ChannelRegisterImpl;
import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.event.config.EventFactory;
import org.kasource.kaevent.event.config.EventFactoryImpl;
import org.kasource.kaevent.event.dispatch.DefaultEventDispatcher;
import org.kasource.kaevent.event.dispatch.DispatcherQueueThread;
import org.kasource.kaevent.event.dispatch.EventMethodInvoker;
import org.kasource.kaevent.event.dispatch.EventMethodInvokerImpl;
import org.kasource.kaevent.event.dispatch.EventRouter;
import org.kasource.kaevent.event.dispatch.EventRouterImpl;
import org.kasource.kaevent.event.dispatch.ThreadPoolQueueExecutor;
import org.kasource.kaevent.event.register.DefaultEventRegister;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegister;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegisterImpl;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class KaEventModule extends AbstractModule implements KaEventInitializedListener{

	

	protected KaEventConfiguration configuration;

	/**
	 * Configure Ka Event with a Java Configuration object, use KaEventConfigBuilder.
	 * 
	 * @param config a configuration object
	 */
	public  KaEventModule(KaEventConfig config) {
		if(config.beanResolver == null) {
			config.beanResolver = new KaEventConfig.BeanResolver();
			config.beanResolver.clazz = GuiceBeanResolver.class.getName();
		}
		KaEventInitializer.getInstance().addListener(this);
		new DefaultEventDispatcher(config);
		
	}
	
	/**
	 * Configure Ka Event with a XML configuration file.
	 * 
	 * @param configLocation	location of the configuration file, supports prefixes <i>file:</i> and <i>classpath:</i>. 
	 **/
	public  KaEventModule(String configLocation) {
		KaEventInitializer.getInstance().addListener(this);
		new DefaultEventDispatcher(configLocation);
		
	}
	
	
	
	
	
	
	@Override
	public void doInitialize(KaEventConfiguration configuration) {
		this.configuration = configuration;
		
	}
	
	@Override
	protected void configure() {
		
	}

	@Provides
	public EventDispatcher provideEventDispatcher() {
		return configuration.getEventDispatcher();
	}

	@Provides
	BeanResolver provideBeanResolver() {
		return configuration.getBeanResolver();
	}

	@Provides
	EventFactory provideEventFactory() {
		return configuration.getEventFactory();
	}

	@Provides
	EventRegister provideEventRegister() {
		return configuration.getEventRegister();
	}

	@Provides
	EventMethodInvoker provideEventMethodInvoker() {
		return configuration.getEventMethodInvoker();
	}

	@Provides
	SourceObjectListenerRegister provideSourceObjectListenerRegister() {
		return configuration.getSourceObjectListenerRegister();
	}

	@Provides
	ChannelRegister provideChannelRegister() {
		return configuration.getChannelRegister();
	}

	@Provides
	EventRouter provideEventRouter() {
		return configuration.getEventRouter();
	}

	@Provides
	ChannelFactory provideChannelFactory() {
		return configuration.getChannelFactory();
	}

	@Provides
	DispatcherQueueThread provideQueueThread() {
		return configuration.getQueueThread();
	}

	@Provides
	KaEventConfiguration provideKaEventConfiguration() {
		return configuration;
	}
	
	

}
