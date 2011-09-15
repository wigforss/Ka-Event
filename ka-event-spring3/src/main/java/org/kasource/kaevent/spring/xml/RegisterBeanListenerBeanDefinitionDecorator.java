package org.kasource.kaevent.spring.xml;

import java.util.EventListener;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.BeanDefinitionDecorator;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

/**
 * Name space handler for attribute <b>listen-on-bean</b>.
 * 
 * Registers the bean which has this attribute as a source object listener.
 * 
 * @author rikardwi
 **/
public class RegisterBeanListenerBeanDefinitionDecorator extends
		AbstractDecorator implements BeanDefinitionDecorator {

	/**
	 * Decorates the object from definition with the node (listen-on-bean) so that the object
	 * hold by the definition is registered as a listener.
	 * 
	 * @param node			The attribute listen-on-bean.
	 * @param definition	The bean definition of the bean with the attribute
	 * @param parserContext The spring context parsed.
	 * 
	 * @return the enhanced definition.
	 **/
	@Override
	public BeanDefinitionHolder decorate(Node node,
										 BeanDefinitionHolder definition, 
										 ParserContext parserContext) {

		RuntimeBeanReference listenerRef = new RuntimeBeanReference(definition.getBeanName());

		String sourceBeans = ((Attr) node).getValue();
		String[] sourceBeanNames = sourceBeans.split(",");
		for (String sourceBeanName : sourceBeanNames) {
			registerBeanListener(parserContext, listenerRef, sourceBeanName.trim());
		}
		return definition;
	}

	/**
	 * Register the listenerRef as listener to the bean with name sourceBeanName.
	 * 
	 * @param parserContext		The spring context parsed.
	 * @param listenerRef		Reference to the definition bean.
	 * @param sourceBeanName	Name of the bean to listen to.
	 **/
	@SuppressWarnings("unchecked")
	private void registerBeanListener(ParserContext parserContext,
									  RuntimeBeanReference listenerRef, 
									  String sourceBeanName) {
		BeanDefinition sol = parserContext.getRegistry().getBeanDefinition("kaEvent.configurer");
		RuntimeBeanReference soureBeanRef = new RuntimeBeanReference(sourceBeanName);
		MutablePropertyValues props = sol.getPropertyValues();
		PropertyValue value = props.getPropertyValue("listeners");
		ManagedMap map = null;
		if (value == null) {
			// Map<Object, List<EventListener>>
			map = new ManagedMap();
			ManagedList list = new ManagedList();
			list.add(listenerRef);
			map.put(soureBeanRef, list);
			props.addPropertyValue("listeners", map);
		} else {
			map = (ManagedMap) value.getValue();

			ManagedList list = (ManagedList) map.get(soureBeanRef);
			if (list == null) {
				list = new ManagedList();
				map.put(soureBeanRef, list);
			}
			list.add(listenerRef);
		}
	}
	
}
