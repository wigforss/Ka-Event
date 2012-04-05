package org.kasource.kaevent.spring.xml;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.junit.Assert.assertEquals;

import org.easymock.Capture;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.kaevent.config.SpringKaEventConfigurer;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;
import org.w3c.dom.Element;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class KaEventConfigurerBeanDefinitionParserTest {

    @TestedObject
    private KaEventConfigurerBeanDefinitionParser parser;
    
    @Mock
    private Element element;
    
    @Mock
    private BeanDefinitionBuilder bean;
    
    @Mock
    private ParserContext pc; 
    
    
    
    @Test
    public void parserTest() {
        Capture<BeanComponentDefinition> beanResolver = new Capture<BeanComponentDefinition>();
        Capture<BeanComponentDefinition> postBeanProcessor = new Capture<BeanComponentDefinition>();
        Capture<BeanComponentDefinition> eventBuilderFactory = new Capture<BeanComponentDefinition>();
        Capture<BeanComponentDefinition> eventRegister = new Capture<BeanComponentDefinition>();
        Capture<BeanComponentDefinition> channelRegister = new Capture<BeanComponentDefinition>();
        Capture<BeanComponentDefinition> sourceObjectListenerRegister = new Capture<BeanComponentDefinition>();
        Capture<BeanComponentDefinition> eventMethodInvoker = new Capture<BeanComponentDefinition>();
        Capture<BeanComponentDefinition> eventRouter = new Capture<BeanComponentDefinition>();
        Capture<BeanComponentDefinition> channelFactory = new Capture<BeanComponentDefinition>();
        Capture<BeanComponentDefinition> eventDispatcher = new Capture<BeanComponentDefinition>();
        Capture<BeanComponentDefinition> configuration = new Capture<BeanComponentDefinition>();
        String scanPath = "pathToScanForEvents";
        element.setAttribute(AbstractSingleBeanDefinitionParser.ID_ATTRIBUTE, "kaEvent.configurer");
        expectLastCall();
        expect(element.getAttribute("scanClassPath")).andReturn(scanPath);
        expect(bean.addPropertyValue("scanClassPath", scanPath)).andReturn(bean);
        expect(bean.addConstructorArgReference(KaEventSpringBean.CONFIGURATION.getId())).andReturn(bean);
        expect(bean.setInitMethodName("configure")).andReturn(bean);
        expect(bean.setLazyInit(false)).andReturn(bean);
        pc.registerBeanComponent(capture(beanResolver));
        expectLastCall();
        pc.registerBeanComponent(capture(postBeanProcessor));
        expectLastCall();
        pc.registerBeanComponent(capture(eventBuilderFactory));
        expectLastCall();
        pc.registerBeanComponent(capture(eventRegister));
        expectLastCall();
        pc.registerBeanComponent(capture(channelRegister));
        expectLastCall();
        pc.registerBeanComponent(capture(sourceObjectListenerRegister));
        expectLastCall();
        pc.registerBeanComponent(capture(eventMethodInvoker));
        expectLastCall();
        pc.registerBeanComponent(capture(eventRouter));
        expectLastCall();
        pc.registerBeanComponent(capture(channelFactory));
        expectLastCall();
        pc.registerBeanComponent(capture(eventDispatcher));
        expectLastCall();
        pc.registerBeanComponent(capture(configuration));
        expectLastCall();
        expect(element.getAttribute("queueBean")).andReturn("true");
        EasyMockUnitils.replay();
        parser.doParse(element, pc, bean);
        
    }
    
    @Test
    public void getClassTest() {
       assertEquals(SpringKaEventConfigurer.class, parser.getBeanClass(element));
    }
}
