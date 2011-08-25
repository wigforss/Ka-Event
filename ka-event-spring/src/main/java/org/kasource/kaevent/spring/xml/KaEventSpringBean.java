package org.kasource.kaevent.spring.xml;

import org.kasource.kaevent.bean.SpringBeanResolver;
import org.kasource.kaevent.channel.ChannelRegisterImpl;
import org.kasource.kaevent.channel.SpringChannelFactory;
import org.kasource.kaevent.config.KaEventConfigurationImpl;
import org.kasource.kaevent.event.config.EventFactoryImpl;
import org.kasource.kaevent.event.dispatch.EventMethodInvokerImpl;
import org.kasource.kaevent.event.dispatch.EventRouterImpl;
import org.kasource.kaevent.event.dispatch.SpringEventDispatcher;
import org.kasource.kaevent.event.dispatch.ThreadPoolQueueExecutor;
import org.kasource.kaevent.event.register.DefaultEventRegister;
import org.kasource.kaevent.listener.register.RegisterListenerBeanPostProcessor;
import org.kasource.kaevent.listener.register.SourceObjectListenerRegisterImpl;

/**
 * Enumeration of bean to create.
 * 
 * @author rikardwi
 **/
public enum KaEventSpringBean {

	BEAN_RESOLVER(SpringBeanResolver.class, "kaEvent.beanResolver",
			      "",
			      new String[]{},
			      new String[]{},
			      new String[]{}),
    REGISTER_LISTENERS(RegisterListenerBeanPostProcessor.class, "kaEvent.postBeanProcessor  ",
			                  "",
			                  new String[]{},
			                  new String[]{},
			                  new String[]{}),
	EVENT_FACTORY(EventFactoryImpl.class,
			"kaEvent.eventFactory", 
			"",
			new String[]{"kaEvent.beanResolver"},
			new String[]{},
			new String[]{}),
	EVENT_REGISTER(DefaultEventRegister.class,
			"kaEvent.eventRegister",
			"",
			new String[]{"kaEvent.eventFactory"},
			new String[]{},
			new String[]{}),
	CHANNEL_REGISTER(ChannelRegisterImpl.class,
			"kaEvent.channelRegister", 
			"",
			new String[]{},
			new String[]{},
			new String[]{}),
	SOURCE_OBJECT_LISTENER_REGISTER(SourceObjectListenerRegisterImpl.class,
			"kaEvent.sourceObjectListenerRegister",
			"",
			new String[]{"kaEvent.eventRegister",
						 "kaEvent.beanResolver"},
			new String[]{},
			new String[]{}),
	EVENT_METHOD_INVOKER(EventMethodInvokerImpl.class,
			"kaEvent.eventMethodInvoker", 
			"",
			new String[]{"kaEvent.eventRegister"},
			new String[]{},
			new String[]{}),
	EVENT_ROUTER(EventRouterImpl.class, 
			"kaEvent.eventRouter", 
			"",
			new String[]{"kaEvent.channelRegister",
					     "kaEvent.sourceObjectListenerRegister", 
					     "kaEvent.eventMethodInvoker"},
			new String[]{},
			new String[]{}),
	CHANNEL_FACTORY(SpringChannelFactory.class,
			"kaEvent.channelFactory", 
			"",
			new String[]{"kaEvent.channelRegister",
						 "kaEvent.eventRegister",
						 "kaEvent.eventMethodInvoker",
						 "kaEvent.beanResolver"},
			new String[]{},
			new String[]{}),
	QUEUE_BEAN(ThreadPoolQueueExecutor.class,
			"kaEvent.eventQueue", 
			"",
			new String[]{"kaEvent.eventRouter"},
			new String[]{},
			new String[]{}),
	EVENT_DISPATCHER(SpringEventDispatcher.class,
			"kaEvent.eventDispatcher", 
			"",
			new String[]{"kaEvent.channelRegister",
						 "kaEvent.channelFactory",
						 "kaEvent.sourceObjectListenerRegister",
						 "kaEvent.eventQueue",
						 "kaEvent.eventRouter"},
			new String[]{},
			new String[]{}),
	CONFIGURATION(KaEventConfigurationImpl.class,
			"kaEvent.configuration", 
			"",
			new String[]{},
			new String[]{"eventDispatcher",
						 "beanResolver",
						 "eventFactory",
						 "eventRegister",
						 "eventMethodInvoker",
						 "sourceObjectListenerRegister",
						 "channelRegister",
						 "eventRouter",
						 "channelFactory",
						 "queueThread"},
			new String[]{"kaEvent.eventDispatcher",
						 "kaEvent.beanResolver",
						 "kaEvent.eventFactory",
						 "kaEvent.eventRegister", 
						 "kaEvent.eventMethodInvoker",
						 "kaEvent.sourceObjectListenerRegister",
						 "kaEvent.channelRegister",
						 "kaEvent.eventRouter",
						 "kaEvent.channelFactory",
						 "kaEvent.eventQueue"});
	
	
	private String id;
	private Class<?> beanClass;
	private String initMethod;
	private String[] constructorRefs;
	private String[] refProperties;
	private String[] propertyRefValues;
	
	/**
	 * Constructor.
	 * 
	 * @param beanClass        Class of bean
	 * @param id               Id of bean
	 * @param initMethod       Init method name of bean.
	 * @param constructorRefs  Constructor ref beans.
	 * @param refProperties     Properties with reference values.
	 * @param propertyRefValues Name of bean references to properties.
	 **/
	KaEventSpringBean(Class<?> beanClass, 
					  String id, 
			          String initMethod,
			          String[] constructorRefs,
			          String[] refProperties, 
			          String[] propertyRefValues) {
		this.beanClass = beanClass;
		this.id = id;
		this.initMethod = initMethod;
		this.constructorRefs = constructorRefs;
		this.refProperties = refProperties;
		this.propertyRefValues = propertyRefValues;
	}

	/**
	 * Returns bean ID.
	 * 
	 * @return bean ID.
	 **/
	public String getId() {
		return id;
	}

	/**
	 * Returns bean class.
	 * 
	 * @return bean class.
	 **/
	public Class<?> getBeanClass() {
		return beanClass;
	}
	
	
   /**
    * Returns Constructor bean references.
    * 
    * @return Constructor bean references.
    */
	public String[] getConstructorRefs() {
		return constructorRefs;
	}

	/** 
	 * Name of bean references to properties.
	 * 
	 * @return Name of bean references to properties.
	 **/
	public String[] getPropertyRefValues() {
		return propertyRefValues;
	}

	/**
	 * Returns Properties with reference values.
     * 
     * @return Properties with reference values.
	 **/
	public String[] getRefProperties() {
		return refProperties;
	}

	/**
	 * Init method name of bean.
	 * 
	 * @return Init method name of bean.
	 **/
	public String getInitMethod() {
		return initMethod;
	}
	    
}
