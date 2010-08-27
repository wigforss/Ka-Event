package org.kasource.kaevent.spring.xml;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionDecorator;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

public class RegisterBeanListenerBeanDefinitionDecorator extends AbstractDecorator  implements BeanDefinitionDecorator{

	@Override
	public BeanDefinitionHolder decorate(Node node,
			BeanDefinitionHolder definition, ParserContext parserContext) {
		 String sourceBeanName = ((Attr) node).getValue();
		 
		return definition;
	}

}
