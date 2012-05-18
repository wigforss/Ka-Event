package org.kasource.kaevent.spring.xml;

import org.kasource.kaevent.event.dispatch.EventQueueFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

public class EventQueueBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
   
    /**
     * Returns the bean class to use.
     * 
     * @param element The channel XML element.
     * 
     * @return The ChannelFactoryBean class.
     **/
    protected Class<?> getBeanClass(Element element) {
       return EventQueueFactoryBean.class;
     }
    
    /**
     * Parses the channel XML element.
     * 
     *  @param element The channel XML element.
     *  @param bean    The bean definition.
     **/
    protected void doParse(Element element, BeanDefinitionBuilder bean) {
         bean.addPropertyValue("name", element.getAttribute(ID_ATTRIBUTE));
         element.setAttribute("lazy-init", "false");
         bean.setLazyInit(false);
        
         if(element.getAttribute("maxThreads") != null && element.getAttribute("maxThreads").length() > 0) {
             bean.addPropertyValue("maxThreads", element.getAttribute("maxThreads"));
         }
         if(element.getAttribute("coreThreads") != null && element.getAttribute("coreThreads").length() > 0) {
             bean.addPropertyValue("coreThreads", element.getAttribute("coreThreads"));
         }
         if(element.getAttribute("keepAliveTime") != null && element.getAttribute("keepAliveTime").length() > 0) {
             bean.addPropertyValue("keepAliveTime", element.getAttribute("keepAliveTime"));
         }
         element.setAttribute("lazy-init", "false");
         
         String ref = element.getAttribute("ref");
         if(ref != null && ref.length() > 0) {
             bean.addPropertyReference("eventQueueRef", ref);
         } else {
             String className = element.getAttribute("class");
             if (className != null) {
                 bean.addPropertyValue("eventQueueClass", className);
             }
         }
     }
}
