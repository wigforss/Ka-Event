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
        registerBeanDefinitionParser("channels", new ChannelsBeanDefinitionParser()); 
    }

}
