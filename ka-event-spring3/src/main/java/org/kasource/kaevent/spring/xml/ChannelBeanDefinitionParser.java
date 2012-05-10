package org.kasource.kaevent.spring.xml;


import java.util.ArrayList;
import java.util.List;

import org.kasource.kaevent.channel.ChannelFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 * Parses the Channel XML Element. 
 * 
 * @author rikardwi
 **/
public class ChannelBeanDefinitionParser  extends AbstractSingleBeanDefinitionParser {
    
    /**
     * Returns the bean class to use.
     * 
     * @param element The channel XML element.
     * 
     * @return The ChannelFactoryBean class.
     **/
	protected Class<?> getBeanClass(Element element) {
       return ChannelFactoryBean.class;
     }

	/**
	 * Parses the channel XML element.
	 * 
     *  @param element The channel XML element.
     *  @param bean    The bean definition.
	 **/
	protected void doParse(Element element, BeanDefinitionBuilder bean) {
         bean.addPropertyValue("name", element.getAttribute(ID_ATTRIBUTE));
         bean.addDependsOn(KaEventSpringBean.CHANNEL_REGISTER.getId());
         bean.addDependsOn(KaEventSpringBean.CHANNEL_FACTORY.getId());
         bean.addDependsOn(KaEventSpringBean.EVENT_REGISTER.getId());
         bean.setLazyInit(false);
         List<Element> handles = DomUtils.getChildElementsByTagName(element, "handle");
         List<String> events = getEventsHandled(handles);
         if (!events.isEmpty()) {
        	 bean.addPropertyValue("events", events);
         }
         String channelRef = element.getAttribute("ref");
         if(channelRef != null && channelRef.length() > 0) {
             bean.addPropertyReference("channelRef", channelRef);
         } else {
             String channelClass = element.getAttribute("class");
             if (channelClass != null) {
                 bean.addPropertyValue("channelClass", channelClass);
             }
         }
     }
     
    /**
     * Returns list of events handled by the channel.
     * 
     * @param handles The handles XML Configuration element.
     * 
     * @return List of events.
     **/
     private List<String> getEventsHandled(List<Element> handles) {
    	 List<String> events = new ArrayList<String>();
    	 if (handles != null) {
    		 for (Element handle : handles) {
    			 events.add(handle.getAttribute("event"));
    		 }
    	 }
    	 return events;
     }
}
