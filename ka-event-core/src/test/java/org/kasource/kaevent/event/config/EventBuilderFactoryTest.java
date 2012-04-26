/**
 * 
 */
package org.kasource.kaevent.event.config;

import java.util.EventObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.event.dispatch.EventQueueRegister;
import org.kasource.kaevent.event.method.MethodResolver;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.InjectInto;
import org.unitils.inject.annotation.TestedObject;

/**
 * @author rikardwigforss
 *
 */
//CHECKSTYLE:OFF
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class EventBuilderFactoryTest {

    
    
    private BeanResolver beanResolver = EasyMockUnitils.createMock(BeanResolver.class);
    
    @InjectInto(property="eventQueueRegister")
    @Mock
    private EventQueueRegister eventQueueRegister;
    
    @SuppressWarnings("rawtypes")
    @Mock
    private MethodResolver methodResolver;
    
    
    @TestedObject
    private EventBuilderFactory factory = new EventBuilderFactoryImpl(beanResolver, eventQueueRegister);
    
   
    @Test
    public void getBuilderTest() {
        factory.getBuilder(EventObject.class);
    }
}
