package org.kasource.kaevent.spring.xml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kasource.kaevent.channel.ChannelFactoryBean;
import org.kasource.kaevent.event.config.EventFactoryBean;
import org.kasource.kaevent.event.method.MethodResolverFactory;
import org.kasource.kaevent.listener.interfaces.MethodResolverType;
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
		String listenerInterface = element.getAttribute("listenerInterface");
		if(listenerInterface != null && listenerInterface.length() > 0) {
			bean.addPropertyValue("listenerClass", element.getAttribute("listenerInterface"));
		}
		bean.addDependsOn(KaEventSpringBean.EVENT_REGISTER.getId());
		bean.addDependsOn(KaEventSpringBean.EVENT_FACTORY.getId());
		
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
		bean.addPropertyValue("methodResolverType", MethodResolverType.FACTORY);
		bean.addPropertyValue("factoryClass", element.getAttribute("factoryClass"));
		bean.addPropertyValue("factoryMethod", element.getAttribute("factoryMethod"));
		String methodArgument = element.getAttribute("factoryMethodArgument");
		if(methodArgument != null && methodArgument.length() > 0) {
			bean.addPropertyValue("factoryMethodArgument", methodArgument);
		}
	}
	
	protected void addBeanMethodResolver(Element element, BeanDefinitionBuilder bean)  {
		bean.addPropertyValue("methodResolverType", MethodResolverType.BEAN);
		String beanName = element.getAttribute("bean");
		bean.addPropertyReference("methodResolver", beanName);
	}
	
	protected void addSwitchMethodResolver(Element element, BeanDefinitionBuilder bean)  {
		bean.addPropertyValue("methodResolverType", MethodResolverType.KEYWORD_SWITCH);
		bean.addPropertyValue("keywordMethodName", element.getAttribute("keywordMethod"));
		
		List<Element> caseElements = DomUtils.getChildElementsByTagName(element,"case");
		Map<String, String> methodMap = getMethods(caseElements);
		bean.addPropertyValue("methodMap", methodMap);
		Element defaultElement = DomUtils.getChildElementByTagName(element,"default");
		bean.addPropertyValue("defaultMethodName", defaultElement.getAttribute("method"));
		
		
		
		
	}
	
	protected Map<String, String> getMethods(List<Element> caseElements) {
		Map<String, String> methodMap = new HashMap<String, String>();
		for(Element caseElement : caseElements) {
			methodMap.put(caseElement.getAttribute("value"), caseElement.getAttribute("method"));
		}
		return methodMap;
	}
	
}
