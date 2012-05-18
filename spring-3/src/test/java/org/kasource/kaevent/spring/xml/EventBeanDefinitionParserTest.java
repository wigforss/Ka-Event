package org.kasource.kaevent.spring.xml;

import static org.easymock.classextension.EasyMock.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class EventBeanDefinitionParserTest {

    @TestedObject
    private EventBeanDefinitionParser parser;
    
    @Mock
    private Element element;
    
    @Mock
    private BeanDefinitionBuilder bean;
    
    @Mock
    private NodeList nodeList;
    
    @Test
    public void doParseTest() {
        String id = "beanId";
        String eventClass = "java.util.EventObject";
        String annotation = "org.MyAnnotation";
        String interfaceClass = "java.utl.EventListener"; 
        expect(element.getAttribute(AbstractSingleBeanDefinitionParser.ID_ATTRIBUTE)).andReturn(id);
        expect(bean.addPropertyValue("name",id)).andReturn(bean);
     
        expect(element.getAttribute("eventClass")).andReturn(eventClass);
        expect(bean.addPropertyValue("eventClass", eventClass)).andReturn(bean);
        
        expect(element.getAttribute("annotation")).andReturn(annotation);
        expect(bean.addPropertyValue("annotation",annotation)).andReturn(bean);
       
        expect(element.getAttribute("listenerInterface")).andReturn(interfaceClass).times(2);
        expect(bean.addPropertyValue("listenerClass",interfaceClass)).andReturn(bean);
        
        expect(bean.addDependsOn(KaEventSpringBean.EVENT_REGISTER.getId())).andReturn(bean);
        expect(bean.addDependsOn(KaEventSpringBean.EVENT_BUILDER_FACTORY.getId())).andReturn(bean);
        // Look for switchMethodResolver
        expect(element.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(0);
        // Look for beanMethodResolver
        expect(element.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(0);
        // Look for factoryMethodResolver
        expect(element.getChildNodes()).andReturn(nodeList);
        expect(nodeList.getLength()).andReturn(0);
        EasyMockUnitils.replay();
        parser.doParse(element, bean);
        
    }
    
}
