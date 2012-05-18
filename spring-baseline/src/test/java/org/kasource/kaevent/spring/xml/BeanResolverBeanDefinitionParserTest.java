package org.kasource.kaevent.spring.xml;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.kaevent.bean.SpringBeanResolver;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;
import org.w3c.dom.Element;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class BeanResolverBeanDefinitionParserTest {

    @TestedObject
    private BeanResolverBeanDefinitionParser parser;
    
    @Mock
    private Element element;
    
    @Mock
    private BeanDefinitionBuilder bean;
    
    @Test
    public void setIdTest() {
        element.setAttribute(AbstractSingleBeanDefinitionParser.ID_ATTRIBUTE, "beanResolver");
        expectLastCall();
        EasyMockUnitils.replay();
        parser.doParse(element, bean);
    }
    
    @Test
    public void validResolverClass() {
        String beanResolver = SpringBeanResolver.class.getName();
        expect(element.getAttribute("class")).andReturn(beanResolver);
        EasyMockUnitils.replay();
        assertEquals(SpringBeanResolver.class, parser.getBeanClass(element));
    }
    
    @Test
    public void NoResolverClass() {     
        expect(element.getAttribute("class")).andReturn(null);
        EasyMockUnitils.replay();
        assertEquals(SpringBeanResolver.class, parser.getBeanClass(element));
    }
    
    @Test(expected = IllegalStateException.class )
    public void IllegalResolverClass() {     
        expect(element.getAttribute("class")).andReturn("java.lang.Integer");
        EasyMockUnitils.replay();
        parser.getBeanClass(element);
    }
}
