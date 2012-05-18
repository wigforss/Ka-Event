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
        Capture<BeanComponentDefinition> eventQueueRegister = new Capture<BeanComponentDefinition>();
        Capture<BeanComponentDefinition> postBeanProcessor = new Capture<BeanComponentDefinition>();
        Capture<BeanComponentDefinition> eventBuilderFactory = new Capture<BeanComponentDefinition>();
        Capture<BeanComponentDefinition> eventRegister = new Capture<BeanComponentDefinition>();
        Capture<BeanComponentDefinition> channelRegister = new Capture<BeanComponentDefinition>();
        Capture<BeanComponentDefinition> sourceObjectListenerRegister = new Capture<BeanComponentDefinition>();
        Capture<BeanComponentDefinition> eventMethodInvoker = new Capture<BeanComponentDefinition>();
        Capture<BeanComponentDefinition> eventRouter = new Capture<BeanComponentDefinition>();
        Capture<BeanComponentDefinition> channelFactory = new Capture<BeanComponentDefinition>();
        Capture<BeanComponentDefinition> eventDispatcher = new Capture<BeanComponentDefinition>();
        Capture<BeanComponentDefinition> queuebean = new Capture<BeanComponentDefinition>();
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
        pc.registerBeanComponent(capture(eventQueueRegister));
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
        pc.registerBeanComponent(capture(queuebean));
        expectLastCall();
        pc.registerBeanComponent(capture(eventDispatcher));
        expectLastCall();
        pc.registerBeanComponent(capture(configuration));
        expectLastCall();
      
       
        EasyMockUnitils.replay();
        parser.doParse(element, pc, bean);
        
        assertEquals(KaEventSpringBean.BEAN_RESOLVER.getBeanClass().getName(), beanResolver.getValue().getBeanDefinition().getBeanClassName());
        assertEquals(KaEventSpringBean.BEAN_RESOLVER.getId(), beanResolver.getValue().getName());
        assertEquals(KaEventSpringBean.EVENT_QUEUE_REGISTER.getBeanClass().getName(), eventQueueRegister.getValue().getBeanDefinition().getBeanClassName());
        assertEquals(KaEventSpringBean.EVENT_QUEUE_REGISTER.getId(), eventQueueRegister.getValue().getName());
        assertEquals(KaEventSpringBean.REGISTER_LISTENERS.getBeanClass().getName(), postBeanProcessor.getValue().getBeanDefinition().getBeanClassName());
        assertEquals(KaEventSpringBean.REGISTER_LISTENERS.getId(), postBeanProcessor.getValue().getName());
        assertEquals(KaEventSpringBean.EVENT_BUILDER_FACTORY.getBeanClass().getName(), eventBuilderFactory.getValue().getBeanDefinition().getBeanClassName());
        assertEquals(KaEventSpringBean.EVENT_BUILDER_FACTORY.getId(), eventBuilderFactory.getValue().getName());
        assertEquals(KaEventSpringBean.EVENT_REGISTER.getBeanClass().getName(), eventRegister.getValue().getBeanDefinition().getBeanClassName());
        assertEquals(KaEventSpringBean.EVENT_REGISTER.getId(), eventRegister.getValue().getName());
        assertEquals(KaEventSpringBean.CHANNEL_REGISTER.getBeanClass().getName(), channelRegister.getValue().getBeanDefinition().getBeanClassName());
        assertEquals(KaEventSpringBean.CHANNEL_REGISTER.getId(), channelRegister.getValue().getName());
        assertEquals(KaEventSpringBean.SOURCE_OBJECT_LISTENER_REGISTER.getBeanClass().getName(), sourceObjectListenerRegister.getValue().getBeanDefinition().getBeanClassName());
        assertEquals(KaEventSpringBean.SOURCE_OBJECT_LISTENER_REGISTER.getId(), sourceObjectListenerRegister.getValue().getName());
        assertEquals(KaEventSpringBean.EVENT_METHOD_INVOKER.getBeanClass().getName(), eventMethodInvoker.getValue().getBeanDefinition().getBeanClassName());
        assertEquals(KaEventSpringBean.EVENT_METHOD_INVOKER.getId(), eventMethodInvoker.getValue().getName());
        assertEquals(KaEventSpringBean.EVENT_ROUTER.getBeanClass().getName(), eventRouter.getValue().getBeanDefinition().getBeanClassName());
        assertEquals(KaEventSpringBean.EVENT_ROUTER.getId(), eventRouter.getValue().getName());
        assertEquals(KaEventSpringBean.CHANNEL_FACTORY.getBeanClass().getName(), channelFactory.getValue().getBeanDefinition().getBeanClassName());
        assertEquals(KaEventSpringBean.CHANNEL_FACTORY.getId(), channelFactory.getValue().getName());
        assertEquals(KaEventSpringBean.QUEUE_BEAN.getBeanClass().getName(), queuebean.getValue().getBeanDefinition().getBeanClassName());
        assertEquals(KaEventSpringBean.QUEUE_BEAN.getId(), queuebean.getValue().getName());
        assertEquals(KaEventSpringBean.EVENT_DISPATCHER.getBeanClass().getName(), eventDispatcher.getValue().getBeanDefinition().getBeanClassName());
        assertEquals(KaEventSpringBean.EVENT_DISPATCHER.getId(), eventDispatcher.getValue().getName());
        assertEquals(KaEventSpringBean.CONFIGURATION.getBeanClass().getName(), configuration.getValue().getBeanDefinition().getBeanClassName());
        assertEquals(KaEventSpringBean.CONFIGURATION.getId(), configuration.getValue().getName());
    }
    
    @Test
    public void getClassTest() {
       assertEquals(SpringKaEventConfigurer.class, parser.getBeanClass(element));
    }
}
