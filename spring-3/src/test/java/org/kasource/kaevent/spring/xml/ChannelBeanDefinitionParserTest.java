package org.kasource.kaevent.spring.xml;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.kaevent.channel.ChannelFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class ChannelBeanDefinitionParserTest {

    @TestedObject
    private ChannelBeanDefinitionParser parser;
    
    @Mock
    private Element element;
    
    @Mock
    private BeanDefinitionBuilder bean;
    
    @Mock
    private NodeList nodeList;
    
    @Test
    public void parseSimpleChannel() {
        String beanId = "beanId";
        expect(element.getAttribute(AbstractSingleBeanDefinitionParser.ID_ATTRIBUTE)).andReturn(beanId);
        expect(bean.addPropertyValue("name", beanId)).andReturn(bean);
        expect(bean.addDependsOn(KaEventSpringBean.CHANNEL_REGISTER.getId())).andReturn(bean);
        expect(bean.addDependsOn(KaEventSpringBean.CHANNEL_FACTORY.getId())).andReturn(bean);
        expect(bean.addDependsOn(KaEventSpringBean.EVENT_REGISTER.getId())).andReturn(bean);
        expect(bean.setLazyInit(false)).andReturn(bean);
        
        expect(element.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(0);
        expectLastCall();
        expect(element.getAttribute("ref")).andReturn(null);
        expect(element.getAttribute("class")).andReturn(null);
        EasyMockUnitils.replay();
        parser.doParse(element, bean);
    }
    
    @Test
    public void getClassTest() {
       assertEquals(ChannelFactoryBean.class, parser.getBeanClass(element));
    }
    
}
