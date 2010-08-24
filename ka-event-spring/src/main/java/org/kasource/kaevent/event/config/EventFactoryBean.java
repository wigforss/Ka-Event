package org.kasource.kaevent.event.config;

import java.util.EventListener;
import java.util.EventObject;

import javax.annotation.Resource;

import org.kasource.kaevent.event.method.MethodResolver;
import org.springframework.beans.factory.FactoryBean;

public class EventFactoryBean implements FactoryBean{

	@Resource
	private EventFactory eventFactory;
	
	private Class<? extends EventObject> eventClass;
	
	private Class<? extends EventListener> listenerClass;
	
	private MethodResolver<EventObject> methodResolver;
	
	private String name;
	
	
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
		if(methodResolver == null) {
			return eventFactory.newFromAnnotatedInterfaceClass(eventClass, listenerClass, name);
		} else {
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

}
