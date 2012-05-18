package org.kasource.kaevent.spring.xml;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.ParserContext;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;
import org.w3c.dom.Attr;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class RegisterBeanListenerBeanDefinitionDecoratorTest {

    @TestedObject
    private RegisterBeanListenerBeanDefinitionDecorator decorator;
    
    @Mock
    private Attr node;
    
    @Mock
    private BeanDefinitionHolder definition;
    
    @Mock
    private ParserContext parserContext;
    
    @Mock
    private BeanDefinitionRegistry registry;
    
    @Mock
    private BeanDefinition configurer;
    
    @Mock
    private MutablePropertyValues configurerProps;
    
    @Mock
    private PropertyValue listenersProperty;
    
    @Test
    public void decorateTest() {
        ManagedMap listeners = new ManagedMap();
        expect(definition.getBeanName()).andReturn("beanName");
        expect(node.getValue()).andReturn("beanToListenTo1");
        expect(parserContext.getRegistry()).andReturn(registry);
        expect(registry.getBeanDefinition("kaEvent.configurer")).andReturn(configurer);
        expect(configurer.getPropertyValues()).andReturn(configurerProps);
        expect(configurerProps.getPropertyValue("listeners")).andReturn(listenersProperty);
        expect(listenersProperty.getValue()).andReturn(listeners);
        EasyMockUnitils.replay();
        decorator.decorate(node, definition, parserContext);
        assertFalse(listeners.isEmpty());
        RuntimeBeanReference source = (RuntimeBeanReference) listeners.keySet().iterator().next();
        assertEquals("beanToListenTo1", source.getBeanName());
        assertFalse(((ManagedList) listeners.get(source)).isEmpty());
        assertEquals(((ManagedList) listeners.get(source)).size(), 1);
        RuntimeBeanReference listenerBeanReference = (RuntimeBeanReference) ((ManagedList) listeners.get(source)).get(0);
        assertEquals("beanName", listenerBeanReference.getBeanName());
    }
    
    @Test
    public void decorateSameBeanTwiceTest() {
        ManagedMap listeners = new ManagedMap();
        expect(definition.getBeanName()).andReturn("beanName");
        expect(node.getValue()).andReturn("beanToListenTo1,beanToListenTo1");
        expect(parserContext.getRegistry()).andReturn(registry).times(2);
        expect(registry.getBeanDefinition("kaEvent.configurer")).andReturn(configurer).times(2);
        expect(configurer.getPropertyValues()).andReturn(configurerProps).times(2);
        expect(configurerProps.getPropertyValue("listeners")).andReturn(listenersProperty).times(2);
        expect(listenersProperty.getValue()).andReturn(listeners).times(2);
        EasyMockUnitils.replay();
        decorator.decorate(node, definition, parserContext);
        assertFalse(listeners.isEmpty());
        RuntimeBeanReference source = (RuntimeBeanReference) listeners.keySet().iterator().next();
        assertEquals("beanToListenTo1", source.getBeanName());
        assertFalse(((ManagedList) listeners.get(source)).isEmpty());
        assertEquals(((ManagedList) listeners.get(source)).size(), 1);
        RuntimeBeanReference listenerBeanReference = (RuntimeBeanReference) ((ManagedList) listeners.get(source)).get(0);
        assertEquals("beanName", listenerBeanReference.getBeanName());
    }
    
    @Test
    public void decorateTwoBeansTest() {
        ManagedMap listeners = new ManagedMap();
        expect(definition.getBeanName()).andReturn("beanName");
        expect(node.getValue()).andReturn("beanToListenTo1,beanToListenTo2");
        expect(parserContext.getRegistry()).andReturn(registry).times(2);
        expect(registry.getBeanDefinition("kaEvent.configurer")).andReturn(configurer).times(2);
        expect(configurer.getPropertyValues()).andReturn(configurerProps).times(2);
        expect(configurerProps.getPropertyValue("listeners")).andReturn(listenersProperty).times(2);
        expect(listenersProperty.getValue()).andReturn(listeners).times(2);
        EasyMockUnitils.replay();
        decorator.decorate(node, definition, parserContext);
        assertFalse(listeners.isEmpty());
        assertTrue(listeners.size() == 2);
        Iterator<?> iterator = listeners.keySet().iterator();
        RuntimeBeanReference source1 = (RuntimeBeanReference) iterator.next();
        assertTrue(source1.getBeanName().equals("beanToListenTo1") || source1.getBeanName().equals("beanToListenTo2"));
        assertFalse(((ManagedList) listeners.get(source1)).isEmpty());
        assertEquals(((ManagedList) listeners.get(source1)).size(), 1);
        assertEquals("beanName", ((RuntimeBeanReference)((ManagedList) listeners.get(source1)).get(0)).getBeanName());
        RuntimeBeanReference source2 = (RuntimeBeanReference) iterator.next();
        assertTrue(source1.getBeanName().equals("beanToListenTo1") || source1.getBeanName().equals("beanToListenTo2"));
        assertFalse(((ManagedList) listeners.get(source2)).isEmpty());
        assertEquals(((ManagedList) listeners.get(source2)).size(), 1);
        assertEquals("beanName", ((RuntimeBeanReference)((ManagedList) listeners.get(source2)).get(0)).getBeanName());
        assertFalse("source1 should not be equal to source2: " + source1.getBeanName() + " = " + source2.getBeanName(),source1.getBeanName().equals(source2.getBeanName()));
    }
    
    @Test
    public void decorateTwoBeansWhiteSpacesTest() {
        ManagedMap listeners = new ManagedMap();
        expect(definition.getBeanName()).andReturn("beanName");
        expect(node.getValue()).andReturn("beanToListenTo1, \t    beanToListenTo2 ");
        expect(parserContext.getRegistry()).andReturn(registry).times(2);
        expect(registry.getBeanDefinition("kaEvent.configurer")).andReturn(configurer).times(2);
        expect(configurer.getPropertyValues()).andReturn(configurerProps).times(2);
        expect(configurerProps.getPropertyValue("listeners")).andReturn(listenersProperty).times(2);
        expect(listenersProperty.getValue()).andReturn(listeners).times(2);
        EasyMockUnitils.replay();
        decorator.decorate(node, definition, parserContext);
        assertFalse(listeners.isEmpty());
        assertTrue(listeners.size() == 2);
        Iterator<?> iterator = listeners.keySet().iterator();
        RuntimeBeanReference source1 = (RuntimeBeanReference) iterator.next();
        assertTrue(source1.getBeanName().equals("beanToListenTo1") || source1.getBeanName().equals("beanToListenTo2"));
        assertFalse(((ManagedList) listeners.get(source1)).isEmpty());
        assertEquals(((ManagedList) listeners.get(source1)).size(), 1);
        assertEquals("beanName", ((RuntimeBeanReference)((ManagedList) listeners.get(source1)).get(0)).getBeanName());
        RuntimeBeanReference source2 = (RuntimeBeanReference) iterator.next();
        assertTrue(source1.getBeanName().equals("beanToListenTo1") || source1.getBeanName().equals("beanToListenTo2"));
        assertFalse(((ManagedList) listeners.get(source2)).isEmpty());
        assertEquals(((ManagedList) listeners.get(source2)).size(), 1);
        assertEquals("beanName", ((RuntimeBeanReference)((ManagedList) listeners.get(source2)).get(0)).getBeanName());
        assertFalse("source1 should not be equal to source2: " + source1.getBeanName() + " = " + source2.getBeanName(),source1.getBeanName().equals(source2.getBeanName()));
    }
    
    @Test
    public void decorateTwoBeansTrailingCommaTest() {
        ManagedMap listeners = new ManagedMap();
        expect(definition.getBeanName()).andReturn("beanName");
        expect(node.getValue()).andReturn("beanToListenTo1,beanToListenTo2,");
        expect(parserContext.getRegistry()).andReturn(registry).times(2);
        expect(registry.getBeanDefinition("kaEvent.configurer")).andReturn(configurer).times(2);
        expect(configurer.getPropertyValues()).andReturn(configurerProps).times(2);
        expect(configurerProps.getPropertyValue("listeners")).andReturn(listenersProperty).times(2);
        expect(listenersProperty.getValue()).andReturn(listeners).times(2);
        EasyMockUnitils.replay();
        decorator.decorate(node, definition, parserContext);
        assertFalse(listeners.isEmpty());
        assertTrue(listeners.size() == 2);
        Iterator<?> iterator = listeners.keySet().iterator();
        RuntimeBeanReference source1 = (RuntimeBeanReference) iterator.next();
        assertTrue(source1.getBeanName().equals("beanToListenTo1") || source1.getBeanName().equals("beanToListenTo2"));
        assertFalse(((ManagedList) listeners.get(source1)).isEmpty());
        assertEquals(((ManagedList) listeners.get(source1)).size(), 1);
        assertEquals("beanName", ((RuntimeBeanReference)((ManagedList) listeners.get(source1)).get(0)).getBeanName());
        RuntimeBeanReference source2 = (RuntimeBeanReference) iterator.next();
        assertTrue(source1.getBeanName().equals("beanToListenTo1") || source1.getBeanName().equals("beanToListenTo2"));
        assertFalse(((ManagedList) listeners.get(source2)).isEmpty());
        assertEquals(((ManagedList) listeners.get(source2)).size(), 1);
        assertEquals("beanName", ((RuntimeBeanReference)((ManagedList) listeners.get(source2)).get(0)).getBeanName());
        assertFalse("source1 should not be equal to source2: " + source1.getBeanName() + " = " + source2.getBeanName(),source1.getBeanName().equals(source2.getBeanName()));
    }
}
