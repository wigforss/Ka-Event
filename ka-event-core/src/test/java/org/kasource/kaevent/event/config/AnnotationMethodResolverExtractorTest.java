/**
 * 
 */
package org.kasource.kaevent.event.config;

import java.util.EventListener;


import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.easymock.EasyMock;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.event.BaseEvent;
import org.kasource.kaevent.event.Event;
import org.kasource.kaevent.event.method.MethodResolver;
import org.kasource.kaevent.listener.interfaces.BeanMethodResolver;
import org.kasource.kaevent.listener.interfaces.FactoryMethodResolver;
import org.kasource.kaevent.listener.interfaces.MethodResolverType;
import org.kasource.kaevent.listener.interfaces.MethodResolving;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;

/**
 * @author rikardwigforss
 *
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class AnnotationMethodResolverExtractorTest {

    @InjectIntoByType
    @Mock
    private BeanResolver beanResolver;
    
    @Mock
    private MethodResolving methodResolving;
    
    @SuppressWarnings("unchecked")
    @Mock
    private MethodResolver methodResolver;
    
    @SuppressWarnings("unchecked")
    @Mock
    private static MethodResolver factoryMethodResolver;
    
    @SuppressWarnings("unchecked")
    @Mock
    private static MethodResolver factoryMethodResolver2;
    
    @TestedObject
    private AnnotationMethodResolverExtractor extractor = new AnnotationMethodResolverExtractor(beanResolver);
    
    @Test(expected=IllegalStateException.class)
    public void getMethodResolverKeywordSwitchWithoutDefaultTest() {
        EasyMock.expect(methodResolving.value()).andReturn(MethodResolverType.KEYWORD_SWITCH);
        EasyMockUnitils.replay();
        extractor.getMethodResolver(ChangeEvent.class, ChangeListener.class, methodResolving);
    }
    
    @Test(expected=IllegalStateException.class)
    public void getMethodResolverBeanNotAnnotatedWithBeanResolverTest() {
        EasyMock.expect(methodResolving.value()).andReturn(MethodResolverType.BEAN);
        EasyMockUnitils.replay();
        extractor.getMethodResolver(ChangeEvent.class, ChangeListener.class, methodResolving);
    }
    
    @Test(expected=IllegalStateException.class)
    public void getMethodResolverFactoryNotAnnotatedWithFactoryResolverTest() {
        EasyMock.expect(methodResolving.value()).andReturn(MethodResolverType.FACTORY);
        EasyMockUnitils.replay();
        extractor.getMethodResolver(ChangeEvent.class, ChangeListener.class, methodResolving);
    }
    
    @Test
    public void getMethodResolverBeanTest() {
        EasyMock.expect(methodResolving.value()).andReturn(MethodResolverType.BEAN);
        EasyMock.expect(beanResolver.getBean("testBean")).andReturn(methodResolver);
        EasyMockUnitils.replay();
        assertEquals(methodResolver, extractor.getMethodResolver(TemperatureBeanChangedEvent.class, TemperatureBeanEventListener.class, methodResolving));
    }
    
    @Test
    public void getMethodResolverFactoryTest() {
        EasyMock.expect(methodResolving.value()).andReturn(MethodResolverType.FACTORY);
        EasyMockUnitils.replay();
        assertEquals(factoryMethodResolver, extractor.getMethodResolver(TemperatureBeanChangedEvent.class, TemperatureFactoryEventListener.class, methodResolving));
    }
    
    @Test
    public void getMethodResolverFactoryWithArgTest() {
        EasyMock.expect(methodResolving.value()).andReturn(MethodResolverType.FACTORY);
        EasyMockUnitils.replay();
        assertEquals(factoryMethodResolver2, extractor.getMethodResolver(TemperatureBeanChangedEvent.class,TemperatureFactoryWithArgEventListener.class, methodResolving));
    }
    
    @Test(expected=IllegalStateException.class)
    public void getMethodResolverFactoryUnknownMethodTest() {
        EasyMock.expect(methodResolving.value()).andReturn(MethodResolverType.FACTORY);
        EasyMockUnitils.replay();
        extractor.getMethodResolver(TemperatureBeanChangedEvent.class, TemperatureFactoryUnknownMethodEventListener.class, methodResolving);
    }
    
    @Test
    public void setBeanResolverTest() {
        extractor.setBeanResolver(beanResolver);
    }
    
    @SuppressWarnings("unchecked")
    public static MethodResolver getResolver() {
        return factoryMethodResolver;
    }
    
    @SuppressWarnings("unchecked")
    public static MethodResolver getResolver(String arg) {
        return factoryMethodResolver2;
    }
    
    @Event(listener=TemperatureBeanEventListener.class)
    ///CLOVER:OFF
    private static class TemperatureBeanChangedEvent extends BaseEvent{
            private static final long serialVersionUID = 1L;

            private double currentTemperature;
            
            public TemperatureBeanChangedEvent(Object source,double currentTemperature) {
                    super(source);
                    this.currentTemperature = currentTemperature;
            }
            
             
             
         public double getCurrentTemperature()
         {
             return this.currentTemperature;
         }
    }
    
    @MethodResolving(MethodResolverType.BEAN)
    @BeanMethodResolver("testBean")
    private static interface TemperatureBeanEventListener extends EventListener{
             public void highTemperature(TemperatureBeanChangedEvent event);
             
             public void mediumTemperature(TemperatureBeanChangedEvent event);
             
             public void lowTemperature(TemperatureBeanChangedEvent event);
    }
    
   
    
    @MethodResolving(MethodResolverType.FACTORY)
    @FactoryMethodResolver(factoryClass=AnnotationMethodResolverExtractorTest.class, factoryMethod="getResolver")
    private static interface TemperatureFactoryEventListener extends EventListener{
             public void highTemperature(TemperatureBeanChangedEvent event);
             
             public void mediumTemperature(TemperatureBeanChangedEvent event);
             
             public void lowTemperature(TemperatureBeanChangedEvent event);
    }
    
    @MethodResolving(MethodResolverType.FACTORY)
    @FactoryMethodResolver(factoryClass=AnnotationMethodResolverExtractorTest.class, factoryMethod="getResolver", factoryMethodArgument="test")
    private static interface TemperatureFactoryWithArgEventListener extends EventListener{
             public void highTemperature(TemperatureBeanChangedEvent event);
             
             public void mediumTemperature(TemperatureBeanChangedEvent event);
             
             public void lowTemperature(TemperatureBeanChangedEvent event);
    }
    
    @MethodResolving(MethodResolverType.FACTORY)
    @FactoryMethodResolver(factoryClass=AnnotationMethodResolverExtractorTest.class, factoryMethod="get")
    private static interface TemperatureFactoryUnknownMethodEventListener extends EventListener{
             public void highTemperature(TemperatureBeanChangedEvent event);
             
             public void mediumTemperature(TemperatureBeanChangedEvent event);
             
             public void lowTemperature(TemperatureBeanChangedEvent event);
    }
}
