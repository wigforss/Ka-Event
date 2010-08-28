package org.kasource.kaevent.spring.xml;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.BeanDefinitionDecorator;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

public class RegisterChannelListenerBeanDefinitionDecorator extends AbstractDecorator implements BeanDefinitionDecorator{

	@SuppressWarnings("unchecked")
	@Override
	public BeanDefinitionHolder decorate(Node node,
			BeanDefinitionHolder definition, ParserContext parserContext) {
		 String channelName = ((Attr) node).getValue();
		 BeanDefinition channel =parserContext.getRegistry().getBeanDefinition(channelName);
		 MutablePropertyValues props = channel.getPropertyValues();
		 PropertyValue value = props.getPropertyValue("listeners");
		 if(value == null) {
			 ManagedList list = new ManagedList();
			 list.add(new RuntimeBeanReference(definition.getBeanName()));
			 props.addPropertyValue("listeners", list);
		 } else {
			 ManagedList list = (ManagedList) value.getValue();
			 list.add(new RuntimeBeanReference(definition.getBeanName()));
		 }
		return definition;
	}

}
