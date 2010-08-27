/**
 * 
 */
package org.kasource.kaevent.spring.xml;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author Rikard Wigforss
 **/
public class KaEventNamespaceHandler extends NamespaceHandlerSupport{
    public void init() {
        registerBeanDefinitionParser("beanResolver", new BeanResolverBeanDefinitionParser()); 
        registerBeanDefinitionParser("channel", new ChannelBeanDefinitionParser()); 
        registerBeanDefinitionParser("event", new EventBeanDefinitionParser()); 
        registerBeanDefinitionParser("kaevent", new KaEventConfigurerBeanDefinitionParser()); 

        registerBeanDefinitionDecoratorForAttribute("listen-on-bean",new RegisterBeanListenerBeanDefinitionDecorator());
        registerBeanDefinitionDecoratorForAttribute("listen-on-channel",new RegisterChannelListenerBeanDefinitionDecorator());
    }

}
