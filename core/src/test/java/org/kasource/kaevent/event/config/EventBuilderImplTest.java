package org.kasource.kaevent.event.config;

import java.util.EventListener;
import java.util.EventObject;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.kaevent.annotations.event.Event;
import org.kasource.kaevent.annotations.event.methodresolving.BeanMethodResolver;
import org.kasource.kaevent.annotations.event.methodresolving.MethodResolverType;
import org.kasource.kaevent.annotations.event.methodresolving.MethodResolving;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.event.BaseEvent;
import org.kasource.kaevent.event.dispatch.EventQueueRegister;
import org.kasource.kaevent.event.method.MethodResolver;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.InjectInto;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class EventBuilderImplTest {

    private BeanResolver beanResolver = EasyMockUnitils.createMock(BeanResolver.class);
    
    @SuppressWarnings("rawtypes")
    @Mock
    private MethodResolver methodResolver;
    
    @InjectInto(property="eventQueueRegister")
    @Mock
    private EventQueueRegister eventQueueRegister;
    
    @TestedObject
    private EventBuilderImpl builder = new EventBuilderImpl(beanResolver, eventQueueRegister, EventObject.class);
    
    @Test
    public void createEventConfig() {
        builder = new EventBuilderImpl(beanResolver, eventQueueRegister, TemperatureBeanChangedEvent.class);
        EasyMock.expect(beanResolver.getBean("testBean", MethodResolver.class)).andReturn(methodResolver);
        EasyMockUnitils.replay();
        builder.build();
    }
    
    @Event(listener = TemperatureBeanEventListener.class)
    ///CLOVER:OFF
    private static class TemperatureBeanChangedEvent extends BaseEvent {
            private static final long serialVersionUID = 1L;

            private double currentTemperature;
            
            public TemperatureBeanChangedEvent(Object source, double currentTemperature) {
                    super(source);
                    this.currentTemperature = currentTemperature;
            }
            
             
             
         @SuppressWarnings("unused")
        public double getCurrentTemperature() {
             return this.currentTemperature;
         }
    }
    
    @MethodResolving(MethodResolverType.BEAN)
    @BeanMethodResolver("testBean")
    private static interface TemperatureBeanEventListener extends EventListener {
             public void highTemperature(TemperatureBeanChangedEvent event);
             
             public void mediumTemperature(TemperatureBeanChangedEvent event);
             
             public void lowTemperature(TemperatureBeanChangedEvent event);
    }
}
