/**
 * 
 */
package org.kasource.kaevent.event.config;

import java.util.EventListener;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.event.BaseEvent;
import org.kasource.kaevent.event.Event;
import org.kasource.kaevent.event.method.MethodResolver;
import org.kasource.kaevent.listener.interfaces.BeanMethodResolver;
import org.kasource.kaevent.listener.interfaces.MethodResolverType;
import org.kasource.kaevent.listener.interfaces.MethodResolving;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;

/**
 * @author rikardwigforss
 *
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class EventFactoryTest {

    
    
    private BeanResolver beanResolver = EasyMockUnitils.createMock(BeanResolver.class);
    
    @SuppressWarnings("unchecked")
    @Mock
    private MethodResolver methodResolver;
    
    
    @TestedObject
    private EventFactory factory = new EventFactoryImpl(beanResolver);
    
    @Test
    public void createEventConfig() {
        EasyMock.expect(beanResolver.getBean("testBean")).andReturn(methodResolver);
        EasyMockUnitils.replay();
        factory.newFromAnnotatedEventClass(TemperatureBeanChangedEvent.class);
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
            
             
             
         @SuppressWarnings("unused")
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
}
