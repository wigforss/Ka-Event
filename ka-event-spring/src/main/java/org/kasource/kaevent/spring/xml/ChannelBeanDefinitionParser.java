package org.kasource.kaevent.spring.xml;


import org.kasource.kaevent.channel.ChannelFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

public class ChannelBeanDefinitionParser  extends AbstractSingleBeanDefinitionParser{
	protected Class<?> getBeanClass(Element element) {
       return ChannelFactoryBean.class;
     }

     protected void doParse(Element element, BeanDefinitionBuilder bean) {
         bean.addPropertyValue("name", element.getAttribute(ID_ATTRIBUTE));
     }
}
