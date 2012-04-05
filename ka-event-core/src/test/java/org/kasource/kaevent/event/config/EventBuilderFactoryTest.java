/**
 * 
 */
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
import org.kasource.kaevent.event.method.MethodResolver;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;

/**
 * @author rikardwigforss
 *
 */
//CHECKSTYLE:OFF
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class EventBuilderFactoryTest {

    
    
    private BeanResolver beanResolver = EasyMockUnitils.createMock(BeanResolver.class);
    
    @SuppressWarnings("rawtypes")
    @Mock
    private MethodResolver methodResolver;
    
    
    @TestedObject
    private EventBuilderFactory factory = new EventBuilderFactoryImpl(beanResolver);
    
   
    @Test
    public void getBuilderTest() {
        factory.getBuilder(EventObject.class);
    }
}
