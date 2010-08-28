package org.kasource.kaevent.spring.xml;

import java.util.EventListener;
import java.util.List;
import java.util.Map;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.BeanDefinitionDecorator;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

public class RegisterBeanListenerBeanDefinitionDecorator extends AbstractDecorator  implements BeanDefinitionDecorator{

	@SuppressWarnings("unchecked")
	@Override
	public BeanDefinitionHolder decorate(Node node,
			BeanDefinitionHolder definition, ParserContext parserContext) {
		 
		RuntimeBeanReference listenerRef = new RuntimeBeanReference(definition.getBeanName());
		
		 String sourceBeanName = ((Attr) node).getValue();
		 RuntimeBeanReference soureBeanRef = new RuntimeBeanReference(sourceBeanName);
		 
		 
		 
		 BeanDefinition sol =parserContext.getRegistry().getBeanDefinition("kaEvent.configurer");
		 
		 MutablePropertyValues props = sol.getPropertyValues();
		 PropertyValue value= props.getPropertyValue("listeners");
		 ManagedMap map = null;
		 if(value == null) {
			 // Map<Object, List<EventListener>>
			 map = new ManagedMap();
			 ManagedList list = new ManagedList();
			 list.add(listenerRef);
			 map.put(soureBeanRef, list);
			 props.addPropertyValue("listeners", map);
		 } else {
			 map = (ManagedMap) value.getValue();
			
			 ManagedList list =(ManagedList) map.get(soureBeanRef);
			 if(list == null) {
				 list = new ManagedList();
				 map.put(soureBeanRef, list);
			 }
			 list.add(listenerRef);
		 }
		
		return definition;
	}

}
