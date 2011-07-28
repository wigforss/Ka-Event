package org.kasource.kaevent.spring.xml;


import java.util.ArrayList;
import java.util.List;

import org.kasource.kaevent.channel.ChannelFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

public class ChannelBeanDefinitionParser  extends AbstractSingleBeanDefinitionParser{
	protected Class<?> getBeanClass(Element element) {
       return ChannelFactoryBean.class;
     }

     @SuppressWarnings("unchecked")
	protected void doParse(Element element, BeanDefinitionBuilder bean) {
         bean.addPropertyValue("name", element.getAttribute(ID_ATTRIBUTE));
         bean.addDependsOn(KaEventSpringBean.CHANNEL_REGISTER.getId());
         bean.addDependsOn(KaEventSpringBean.CHANNEL_FACTORY.getId());
         bean.addDependsOn(KaEventSpringBean.EVENT_REGISTER.getId());
         element.setAttribute("lazy-init", "false");
         List<Element> handles = DomUtils.getChildElementsByTagName(element,"handle");
         List<String> events = getEventsHandled(handles);
         if(!events.isEmpty()) {
        	 bean.addPropertyValue("events", events);
         }
         String channelClass = element.getAttribute("class");
         if(channelClass != null) {
        	 bean.addPropertyValue("channelClass", channelClass);
         }
     }
     
     protected List<String> getEventsHandled(List<Element> handles) {
    	 List<String> events = new ArrayList<String>();
    	 if(handles != null) {
    		 for(Element handle : handles) {
    			 events.add(handle.getAttribute("event"));
    		 }
    	 }
    	 return events;
     }
}
