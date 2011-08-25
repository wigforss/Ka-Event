package org.kasource.kaevent.event.config;

import java.util.EventListener;
import java.util.EventObject;
import java.util.Map;

import org.kasource.kaevent.annotations.event.methodresolving.MethodResolverType;
import org.kasource.kaevent.event.method.MethodResolver;
import org.kasource.kaevent.event.method.MethodResolverFactory;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.spring.xml.KaEventSpringBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Spring Event Factory Bean.
 * 
 * @author rikardwi
 **/
public class EventFactoryBean implements FactoryBean, ApplicationContextAware {

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
	
	/**
	 * Set event class.
	 * 
	 * @param eventClass Event Class.
	 **/
	public void setEventClass(Class<? extends EventObject> eventClass) {
		this.eventClass = eventClass;
	}

	/**
	 * Set the Event Listener Interface class.
	 * 
	 * @param listenerClass Event Listener Interface class.
	 **/
	public void setListenerClass(Class<? extends EventListener> listenerClass) {
		this.listenerClass = listenerClass;
	}

	
	/**
	 * Set name of the event.
	 * 
	 * @param name Event name.
	 **/
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Object getObject() throws Exception {
		EventConfig eventConfig = getEventConfig();
		 EventRegister eventRegister = 
			 (EventRegister) applicationContext.getBean(KaEventSpringBean.EVENT_REGISTER.getId());
		 eventRegister.registerEvent(eventConfig);
		return eventConfig;
		
	}

	/**
	 * Returns an Event Configuration object for this event.
	 * 
	 * @return new Event Configuration instance.
	 **/
	private EventConfig getEventConfig() {
		 EventFactory eventFactory = 
			 (EventFactory) applicationContext.getBean(KaEventSpringBean.EVENT_FACTORY.getId());
			if (methodResolverType == null) {
				if (listenerClass == null) {
					return eventFactory.newFromAnnotatedEventClass(eventClass, name);
				} else {
					return eventFactory
					   .newFromAnnotatedInterfaceClass(eventClass, 
							                           listenerClass, 
							                           name);
				}
			} else {
			    setMethodResolver();
				return eventFactory
				   .newWithMethodResolver(eventClass, 
						                  listenerClass, 
						                  methodResolver, 
						                  name);
				
			}
	}

	/**
	 * Sets the method resolver for this event
	 * depending on the methodResolverType.
	 **/
	@SuppressWarnings("unchecked")
    private void setMethodResolver() {
	    switch (methodResolverType) {
        case BEAN:
            if (methodResolver == null) {
                throw new IllegalStateException("No method resolver found!!");
            }
            break;
        case FACTORY:
            methodResolver = MethodResolverFactory
            .getFromFactoryMethod(factoryClass, 
                                  factoryMethod, 
                                  factoryMethodArgument);
            break;
        case KEYWORD_SWITCH:
            methodResolver = MethodResolverFactory
               .newKeywordSwitch(eventClass, 
                                 listenerClass, 
                                 keywordMethodName, 
                                 methodMap, 
                                 defaultMethodName);
            break;
        default:
            
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

	
	/**
	 * Set the methodResolver to use.
	 * 
	 * @param methodResolver Method Resolver.
	 **/
	public void setMethodResolver(MethodResolver<EventObject> methodResolver) {
		this.methodResolver = methodResolver;
	}

	/**
	 * Set the type of Method Resolving.
	 * 
	 * @param methodResolverType Method Resolver Type.
	 **/
	public void setMethodResolverType(MethodResolverType methodResolverType) {
		this.methodResolverType = methodResolverType;
	}

	/**
	 * Set Factory class to use when finding method resolver.
	 * 
	 * @param factoryClass Method Resolver Factory Class.
	 **/
	public void setFactoryClass(Class<?> factoryClass) {
		this.factoryClass = factoryClass;
	}

	/**
	 * Method to invoke to get hold of the method resolver to use.
	 * 
	 * @param factoryMethod Method Resolver static factory method.
	 **/
	public void setFactoryMethod(String factoryMethod) {
		this.factoryMethod = factoryMethod;
	}

	/**
	 * Argument to static factory method used to get hold of a MethodResolver from factory.
	 * 
	 * @param factoryMethodArgument Argument to Method Resolver factory method.
	 **/
	public void setFactoryMethodArgument(String factoryMethodArgument) {
		this.factoryMethodArgument = factoryMethodArgument;
	}

	/**
	 * Name of the keyword method when configuring a keyword method resolver.
	 * 
	 * @param keywordMethodName Name of the keyword method (from event class).
	 **/
	public void setKeywordMethodName(String keywordMethodName) {
		this.keywordMethodName = keywordMethodName;
	}

	/**
	 * Set method map to use when configuring a keyword method resolver.
	 * 
	 * @param methodMap Map of method names.
	 **/
	public void setMethodMap(Map<String, String> methodMap) {
		this.methodMap = methodMap;
	}

	/**
     * Set default method name to use when configuring a keyword method resolver.
     * 
     * @param defaultMethodName Name of the default method.
     **/
	public void setDefaultMethodName(String defaultMethodName) {
		this.defaultMethodName = defaultMethodName;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
		
	}

}
