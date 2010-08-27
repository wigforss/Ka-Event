package org.kasource.kaevent.event.config;

import java.util.EventListener;
import java.util.EventObject;
import java.util.Map;

import javax.annotation.Resource;

import org.kasource.kaevent.event.method.MethodResolver;
import org.kasource.kaevent.event.method.MethodResolverFactory;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.listener.interfaces.MethodResolverType;
import org.kasource.kaevent.spring.xml.KaEventSpringBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class EventFactoryBean implements FactoryBean, ApplicationContextAware{

	private Class<? extends EventObject> eventClass;
	
	private Class<? extends EventListener> listenerClass;
	
	private MethodResolver<EventObject> methodResolver;
	
	private String name;
	
	private MethodResolverType methodResolverType;
	
	private Class<?> factoryClass;
	private String factoryMethod;
	private String factoryMethodArgument;
	
	private String keywordMethodName;
	private Map<String, String> methodMap;
	private String defaultMethodName;
	
	private ApplicationContext applicationContext;
	
	public void setEventClass(Class<? extends EventObject> eventClass) {
		this.eventClass = eventClass;
	}

	public void setListenerClass(Class<? extends EventListener> listenerClass) {
		this.listenerClass = listenerClass;
	}

	
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Object getObject() throws Exception {
		EventConfig eventConfig = getEventConfig();
		 EventRegister eventRegister = (EventRegister) applicationContext.getBean(KaEventSpringBean.EVENT_REGISTER.getId());
		 eventRegister.registerEvent(eventConfig);
		return eventConfig;
		
	}

	private EventConfig getEventConfig() {
		 EventFactory eventFactory = (EventFactory) applicationContext.getBean(KaEventSpringBean.EVENT_FACTORY.getId());
			if(methodResolverType == null) {
				if(listenerClass == null) {
					return eventFactory.newFromAnnotatedEventClass(eventClass, name);
				} else {
					return eventFactory.newFromAnnotatedInterfaceClass(eventClass, listenerClass, name);
				}
			} else {
				switch(methodResolverType) {
				case BEAN:
					if(methodResolver == null) {
						throw new IllegalStateException("No method resolver found!!");
					}
					break;
				case FACTORY:
					methodResolver = MethodResolverFactory.getFromFactoryMethod(factoryClass, factoryMethod, factoryMethodArgument);
					break;
				case KEYWORD_SWITCH:
					methodResolver = MethodResolverFactory.newKeywordSwitch(eventClass, listenerClass, keywordMethodName, methodMap, defaultMethodName);
					break;
				}
				return eventFactory.newWithMethodResolver(eventClass, listenerClass, methodResolver, name);
			}
	}

	@Override
	public Class<?> getObjectType() {
		
		return EventConfig.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	

	public void setMethodResolver(MethodResolver<EventObject> methodResolver) {
		this.methodResolver = methodResolver;
	}

	public void setMethodResolverType(MethodResolverType methodResolverType) {
		this.methodResolverType = methodResolverType;
	}

	public void setFactoryClass(Class<?> factoryClass) {
		this.factoryClass = factoryClass;
	}

	public void setFactoryMethod(String factoryMethod) {
		this.factoryMethod = factoryMethod;
	}

	public void setFactoryMethodArgument(String factoryMethodArgument) {
		this.factoryMethodArgument = factoryMethodArgument;
	}

	public void setKeywordMethodName(String keywordMethodName) {
		this.keywordMethodName = keywordMethodName;
	}

	public void setMethodMap(Map<String, String> methodMap) {
		this.methodMap = methodMap;
	}

	public void setDefaultMethodName(String defaultMethodName) {
		this.defaultMethodName = defaultMethodName;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
		
	}

}
