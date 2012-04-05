package org.kasource.kaevent.spring.xml;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.BeanDefinitionDecorator;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

/**
 * Name space handler for attribute <b>listen-on-channel</b>.
 * 
 * Registers the bean which has this attribute as a channel listener.
 * 
 * @author rikardwi
 **/
public class RegisterChannelListenerBeanDefinitionDecorator 
    extends AbstractDecorator implements BeanDefinitionDecorator {

	/**
	 * Decorates the object from definition with the node (listen-on-channel) so that the object
	 * hold by the definition is registered as a listener.
	 * 
	 * @param node			The attribute listen-on-bean.
	 * @param definition	The bean definition of the bean with the attribute
	 * @param parserContext The spring context parsed.
	 * 
	 * @return The decorated bean definition.
	 **/
	@Override
	public BeanDefinitionHolder decorate(Node node,
			BeanDefinitionHolder definition, ParserContext parserContext) {
		 String channels = ((Attr) node).getValue();
	
		 String[] channelNames = channels.split(",");
		 for (String channelName : channelNames) {
			 setListener(channelName, definition, parserContext);
			// addFilters(channelName, definition, parserContext, filters);
		 }
		return definition;
	}

	/**
	 * Register the bean hold by the definition as a listener to channelName.
	 * 
	 * @param channelName	Name of the channel to listen to.
	 * @param definition	The object to register as a listener.
	 * @param parserContext	The spring context parsed.
	 **/
	@SuppressWarnings("unchecked")
	private void setListener(String channelName,
			BeanDefinitionHolder definition,
			ParserContext parserContext) {
		BeanDefinition channel = parserContext.getRegistry().getBeanDefinition(channelName);
		 MutablePropertyValues props = channel.getPropertyValues();
		PropertyValue value = props.getPropertyValue("listeners");
		 if (value == null) {
			 ManagedList<RuntimeBeanReference> list = new ManagedList<RuntimeBeanReference>();
			 list.add(new RuntimeBeanReference(definition.getBeanName()));
			 props.addPropertyValue("listeners", list);
		 } else {
			 ManagedList<RuntimeBeanReference> list = (ManagedList<RuntimeBeanReference>) value.getValue();
			 list.add(new RuntimeBeanReference(definition.getBeanName()));
		 }
	}
}
