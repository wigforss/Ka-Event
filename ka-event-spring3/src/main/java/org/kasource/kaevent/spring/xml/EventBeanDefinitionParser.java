package org.kasource.kaevent.spring.xml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kasource.kaevent.annotations.event.methodresolving.MethodResolverType;
import org.kasource.kaevent.event.config.EventFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 * Handler for the Event XML element.
 * 
 * @author rikardwi
 **/
public class EventBeanDefinitionParser extends
		AbstractSingleBeanDefinitionParser {
    /**
     * Returns EventFactoryBean.
     * 
     * @param element event XML Element.
     * 
     * @return EventFactoryBean.
     **/
	protected Class<?> getBeanClass(Element element) {
		return EventFactoryBean.class;
	}

	/**
	 * Parse the event XML Element.
	 * 
	 * @param element event XML Element.
	 * @param bean    bean definition.
	 **/
	protected void doParse(Element element, BeanDefinitionBuilder bean) {
		bean.addPropertyValue("name", element.getAttribute(ID_ATTRIBUTE));
		bean.addPropertyValue("eventClass", element.getAttribute("eventClass"));
		String listenerInterface = element.getAttribute("listenerInterface");
		if (listenerInterface != null && listenerInterface.length() > 0) {
			bean.addPropertyValue("listenerClass", element.getAttribute("listenerInterface"));
		}
		bean.addDependsOn(KaEventSpringBean.EVENT_REGISTER.getId());
		bean.addDependsOn(KaEventSpringBean.EVENT_FACTORY.getId());
		
		Element switchMethodResolver = DomUtils.getChildElementByTagName(element, "switchMethodResolver");
		if (switchMethodResolver != null) {
			addSwitchMethodResolver(switchMethodResolver, bean);
		} else {
			Element beanMethodResolver = DomUtils.getChildElementByTagName(element, "beanMethodResolver");
			if (beanMethodResolver != null) {
				addBeanMethodResolver(beanMethodResolver, bean);
			} else {
			    addFactoryMethodResolver(element, bean);
			}
		}
	}
	
	/**
	 * Adds a method resolver by invoking static factory method.
	 * 
	 * @param element event XML Element.
     * @param bean    bean definition.
	 **/
	private void addFactoryMethodResolver(Element element, BeanDefinitionBuilder bean)  {
	    Element factoryMethodResolver = DomUtils.getChildElementByTagName(element, "factoryMethodResolver");
	    if (factoryMethodResolver != null) {
	        bean.addPropertyValue("methodResolverType", MethodResolverType.FACTORY);
	        bean.addPropertyValue("factoryClass", factoryMethodResolver.getAttribute("factoryClass"));
	        bean.addPropertyValue("factoryMethod", factoryMethodResolver.getAttribute("factoryMethod"));
	        String methodArgument = factoryMethodResolver.getAttribute("factoryMethodArgument");
	        if (methodArgument != null && methodArgument.length() > 0) {
	            bean.addPropertyValue("factoryMethodArgument", methodArgument);
	        }
	    }
		
	}
	
	/**
	 * Adds a method resolver by looking up a bean.
	 * 
     * @param element event XML Element.
     * @param bean    bean definition.
	 **/
	private void addBeanMethodResolver(Element element, BeanDefinitionBuilder bean)  {
		bean.addPropertyValue("methodResolverType", MethodResolverType.BEAN);
		String beanName = element.getAttribute("bean");
		bean.addPropertyReference("methodResolver", beanName);
	}
	
	/**
	 * Adds a keyword switch method resolver by XML configuration.
	 * 
     * @param element event XML Element.
     * @param bean    bean definition.
	 **/
	private void addSwitchMethodResolver(Element element, BeanDefinitionBuilder bean)  {
		bean.addPropertyValue("methodResolverType", MethodResolverType.KEYWORD_SWITCH);
		bean.addPropertyValue("keywordMethodName", element.getAttribute("keywordMethod"));
		
		List<Element> caseElements = DomUtils.getChildElementsByTagName(element, "case");
		Map<String, String> methodMap = getMethods(caseElements);
		bean.addPropertyValue("methodMap", methodMap);
		Element defaultElement = DomUtils.getChildElementByTagName(element, "default");
		bean.addPropertyValue("defaultMethodName", defaultElement.getAttribute("method"));
			
	}
	
	/**
	 * Returns a map of method names from the List of XML case elements.
	 * 
	 * @param caseElements List of XML case elements.
	 * 
	 * @return Map of method names.
	 **/
	private Map<String, String> getMethods(List<Element> caseElements) {
		Map<String, String> methodMap = new HashMap<String, String>();
		for (Element caseElement : caseElements) {
			methodMap.put(caseElement.getAttribute("value"), caseElement.getAttribute("method"));
		}
		return methodMap;
	}
	
}
