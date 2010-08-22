/**
 * 
 */
package org.kasource.kaevent.spring.xml;

import java.util.List;

import org.kasource.kaevent.channel.ChannelImpl;
import org.kasource.kaevent.channel.ChannelFactoryBean;
import org.kasource.kaevent.channel.ChannelRegisterImpl;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;


/**
 * @author rikardwigforss
 *
 */
public class ChannelsBeanDefinitionParser extends AbstractSingleBeanDefinitionParser{
    
//    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
//        BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(ChannelRegisterFactoryBean.class);
//        BeanDefinitionBuilder channelRegister = BeanDefinitionBuilder.rootBeanDefinition(ChannelRegisterImpl.class);
//        factory.addPropertyValue("channelRegister", channelRegister.getBeanDefinition());
//
//        List<Element> childElements = DomUtils.getChildElementsByTagName(element, "channel");
//        if (childElements != null && childElements.size() > 0) {
//            parseChildChannels(childElements, factory);
//        }
//        return factory.getBeanDefinition();
//     }
//
//    private static void parseChildChannels(List<Element> childElements, BeanDefinitionBuilder factory) {
//        ManagedList channels = new ManagedList(childElements.size());
//        for (int i = 0; i < childElements.size(); ++i) {
//           Element childElement = (Element) childElements.get(i);
//           BeanDefinitionBuilder child = parseChannel(childElement);
//           channels.add(child.getBeanDefinition());
//        }
//        factory.addPropertyValue("channels", channels);
//     }
//
//    private static BeanDefinitionBuilder parseChannel(Element element) {
//        BeanDefinitionBuilder component = BeanDefinitionBuilder.rootBeanDefinition(ChannelImpl.class);
//        component.addPropertyValue("name", element.getAttribute("name"));
//        return component;
//     }


    

}
