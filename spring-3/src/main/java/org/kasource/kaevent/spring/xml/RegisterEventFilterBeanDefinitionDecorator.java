package org.kasource.kaevent.spring.xml;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.BeanCreationException;
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
 * Name space handler for attribute <b>event-filter</b>.
 * 
 * Registers event filters to all listener registrations.
 * 
 * Note: this attribute should only be used together with either 
 * listen-on-channel or listen-on-bean. If  none of these attribute is set an exception
 * will be thrown.
 * 
 * @author rikardwi
 **/
public class RegisterEventFilterBeanDefinitionDecorator extends AbstractDecorator implements BeanDefinitionDecorator {

	/**
	 * Decorates the object from definition with the node (event-filter) so that the object
	 * hold by the definition has event filters registered to its listener.
	 * 
	 * @param node			The attribute event-filter.
	 * @param definition	The bean definition of the bean with the attribute
	 * @param parserContext The spring context parsed.
	 * 
	 * @return decorated bean definition.
	 **/
	@Override
	public BeanDefinitionHolder decorate(Node node,
			BeanDefinitionHolder definition, ParserContext parserContext) {
		String filterNames = ((Attr) node).getValue();
		if (filterNames != null) {
			String channels = getAttributeValue("listen-on-channel", ((Attr) node).getOwnerElement());
			registerChannelListenerFilter(channels, filterNames, definition, parserContext);

			String beans = getAttributeValue("listen-on-bean", ((Attr) node).getOwnerElement());
			
			registerBeanListenerFilter(beans, filterNames, definition,
						parserContext);
			
			if (channels == null && beans == null) {
				throw new BeanCreationException(
						definition.getBeanName(),
						"event-filter is used without being a listener, "
						+ "this bean needs to have either listen-on-channel "
								+ "or listen-on-bean attribute set.");
			}
		}
		return definition;
	}

	/**
	 * Register event filters for channels.
	 * 
	 * @param channels      Channels to register filters for.
	 * @param filters       Filter to register.
	 * @param definition    Bean definition.
	 * @param parserContext Parser Context.
	 **/
	private void registerChannelListenerFilter(String channels, 
	                                           String filters,
	                                           BeanDefinitionHolder definition,
	                                           ParserContext parserContext) {
	    if (channels != null) {
	        String[] channelNames = channels.split(",");
	        for (String channelName : channelNames) {
	            BeanDefinition channel = parserContext.getRegistry().getBeanDefinition(channelName);
	            MutablePropertyValues props = channel.getPropertyValues();
	            PropertyValue value = props.getPropertyValue("filterMap");
	            addFilters(filters, definition, props, value);
	        }
	    }
	}

	
	/**
	 * Register Event filter for beans (source objects).
	 * 
	 * @param beans         Source objects to listen to.
	 * @param filters       Filters to add.
	 * @param definition    Bean Definition.
	 * @param parserContext Parser Context.
	 **/
	private void registerBeanListenerFilter(String beans,
	                                        String filters, 
	                                        BeanDefinitionHolder definition, 
	                                        ParserContext parserContext) {
	    if (beans != null) {
	        BeanDefinition sol = parserContext.getRegistry().getBeanDefinition("kaEvent.configurer");
	        MutablePropertyValues props = sol.getPropertyValues();
	        PropertyValue value = props.getPropertyValue("listeners");
	        addFilters(filters, definition, props, value);
	    }
	}
	
	/**
	 * Add filters. 
	 * 
	 * @param filters    Filters.
	 * @param definition bean
	 * @param props      Properties for bean.
	 * @param value      listeners property value.
	 **/
	@SuppressWarnings("unchecked")
	private void addFilters(String filters, BeanDefinitionHolder definition,
			MutablePropertyValues props, PropertyValue value) {
		ManagedMap<RuntimeBeanReference, ManagedList<RuntimeBeanReference>> filterMap = null;
		if (value == null) {
			filterMap = new ManagedMap<RuntimeBeanReference, ManagedList<RuntimeBeanReference>>();
			props.addPropertyValue("filterMap", filterMap);
		} else {
			filterMap = (ManagedMap<RuntimeBeanReference, ManagedList<RuntimeBeanReference>>) value.getValue();
		}
		String[] filterNames = filters.split(",");
		ManagedList<RuntimeBeanReference> list = new ManagedList<RuntimeBeanReference>();
		for (String filterName : filterNames) {
			list.add(new RuntimeBeanReference(filterName.trim()));
		}
		filterMap.put(new RuntimeBeanReference(definition.getBeanName()), list);
	}
}
