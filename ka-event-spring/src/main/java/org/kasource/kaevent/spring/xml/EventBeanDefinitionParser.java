package org.kasource.kaevent.spring.xml;

import org.kasource.kaevent.channel.ChannelFactoryBean;
import org.kasource.kaevent.event.config.EventFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

public class EventBeanDefinitionParser extends
		AbstractSingleBeanDefinitionParser {
	protected Class<?> getBeanClass(Element element) {
		return EventFactoryBean.class;
	}

	protected void doParse(Element element, BeanDefinitionBuilder bean) {
		bean.addPropertyValue("name", element.getAttribute(ID_ATTRIBUTE));
		bean.addPropertyValue("eventClass", element.getAttribute("eventClass"));
		bean.addPropertyValue("listenerClass", element.getAttribute("listenerInterface"));
		Element switchMethodResolver = DomUtils.getChildElementByTagName(element, "switchMethodResolver");
		if(switchMethodResolver != null) {
			addSwitchMethodResolver(switchMethodResolver,bean);
		} else {
			Element beanMethodResolver = DomUtils.getChildElementByTagName(element, "beanMethodResolver");
			if(beanMethodResolver != null) {
				addBeanMethodResolver(beanMethodResolver, bean);
			} else {
				Element factoryMethodResolver = DomUtils.getChildElementByTagName(element, "factoryMethodResolver");
				if(factoryMethodResolver != null) {
					addFactoryMethodResolver(factoryMethodResolver, bean);
				}
			}
		}
	}
	
	protected void addFactoryMethodResolver(Element element, BeanDefinitionBuilder bean)  {
		String factoryClass = element.getAttribute("factoryClass");
		String factoryMethod = element.getAttribute("factoryMethod");
		String methodArgument = element.getAttribute("factoryMethodArgument");
	}
	
	protected void addBeanMethodResolver(Element element, BeanDefinitionBuilder bean)  {
		
	}
	
	protected void addSwitchMethodResolver(Element element, BeanDefinitionBuilder bean)  {
		
	}
	
}
