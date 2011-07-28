package org.kasource.kaevent.spring.xml;

import java.util.Arrays;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Shared utility methods.
 * 
 * TODO: Consider moving to utility classes instead.
 * 
 * @author rikardwi
 **/
public abstract class AbstractDecorator {

	private static final String KA_EVENT_NS = "http://kasource.org/schema/kaevent-spring";

	/**
	 * Adds the bean with the beanName as a dependency to the bean 
	 * 
	 * @param bean		The bean that is changed
	 * @param beanName	Name of the bean that bean should depend on
	 **/
	protected void addDependsOn(BeanDefinitionHolder bean, String beanName) {
		AbstractBeanDefinition definition = ((AbstractBeanDefinition) bean.getBeanDefinition());
		  String[] dependsOn = definition.getDependsOn();
		  
		  if(dependsOn == null || dependsOn.length == 0) {
			  definition.setDependsOn(new String[]{beanName});
		  } else {
			  String[] newDepends = Arrays.copyOf(dependsOn, dependsOn.length+1);
			  newDepends[newDepends.length-1] = beanName;
			  definition.setDependsOn(new String[]{beanName});
		  }
	}
	
	/**
	 * Returns the prefix for ka-event, null if ka-event does not have
	 * a prefix in the current XML Document.
	 * 	
	 * @param node Node to inspect.
	 * 
	 * @return Name of ka-event xml prefix or null.
	 **/
	protected String getNamespacePrefix(Node node) {
		Document doc = node.getOwnerDocument();
		return doc.lookupPrefix(KA_EVENT_NS);
	}
	
	/**
	 * Returns the value of an ka-event attribute from the Element, null
	 * if no attribute found.
	 *  
	 * @param attributeName	Attribute to get value for.
	 * @param element		The element holding the attributes.
	 * 
	 * @return	The value of the attribute or null if no attribute found.
	 **/
	protected String getAttributeValue(String attributeName, Element element) {
		NamedNodeMap attributes = element.getAttributes();
		String prefix = getNamespacePrefix(element);
		Node node = null;
		if(prefix == null) {
			node = attributes.getNamedItem(attributeName);
		} else {
			node = attributes.getNamedItem(prefix+":"+attributeName);
		}
		if(node != null) {
			return node.getNodeValue();
		}
		return null;
	}
}
