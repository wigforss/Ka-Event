/**
 * Name space handler form ka-event spring name space.
 * 
 * @author wigforss
 **/
package org.kasource.kaevent.spring.xml;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author Rikard Wigforss
 **/
public class KaEventNamespaceHandler extends NamespaceHandlerSupport {
    
    /**
     * Initialize the name space handler.
     **/
    public void init() {
        registerBeanDefinitionParser("beanResolver", new BeanResolverBeanDefinitionParser()); 
        registerBeanDefinitionParser("channel", new ChannelBeanDefinitionParser()); 
        registerBeanDefinitionParser("event", new EventBeanDefinitionParser()); 
        registerBeanDefinitionParser("kaevent", new KaEventConfigurerBeanDefinitionParser()); 
        registerBeanDefinitionParser("eventQueue", new EventQueueBeanDefinitionParser());
        registerBeanDefinitionDecoratorForAttribute("listen-on-bean", 
        		new RegisterBeanListenerBeanDefinitionDecorator());
        registerBeanDefinitionDecoratorForAttribute("listen-on-channel", 
        		new RegisterChannelListenerBeanDefinitionDecorator());
        registerBeanDefinitionDecoratorForAttribute("filter", 
        		new RegisterEventFilterBeanDefinitionDecorator());
        
		
    }

}
