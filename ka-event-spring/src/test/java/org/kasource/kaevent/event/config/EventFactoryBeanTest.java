package org.kasource.kaevent.event.config;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.EventObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.kaevent.event.config.EventBuilder;
import org.kasource.kaevent.event.config.EventBuilderFactory;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.spring.xml.KaEventSpringBean;
import org.springframework.context.ApplicationContext;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.InjectInto;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.annotation.Dummy;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class EventFactoryBeanTest {

    @InjectIntoByType
    @Mock
    private ApplicationContext applicationContext;
    
    @Mock
    private EventBuilderFactory eventBuilderFactory;
    
    @Mock
    private EventBuilder eventBuilder;
    
    @Mock
    private EventRegister eventRegister;
    
    @InjectInto(property = "eventClass")
    private Class<? extends EventObject> eventClass = EventObject.class;
    
    @InjectInto(property = "name")
    private String name = "TestEvent";
    
    @Dummy
    private EventConfig eventConfig;
    
    @TestedObject
    private EventFactoryBean factory;
    
    @Test
    public void getObjectTest() throws Exception {
        expect(applicationContext.getBean(KaEventSpringBean.EVENT_BUILDER_FACTORY.getId())).andReturn(eventBuilderFactory);
        expect(eventBuilderFactory.getBuilder(eventClass)).andReturn(eventBuilder);
        expect(eventBuilder.name(name)).andReturn(eventBuilder);
        expect(eventBuilder.build()).andReturn(eventConfig);
        expect(applicationContext.getBean(KaEventSpringBean.EVENT_REGISTER.getId())).andReturn(eventRegister);
        eventRegister.registerEvent(eventConfig);
        expectLastCall();
        EasyMockUnitils.replay();
        assertEquals(eventConfig, factory.getObject());
    }
}
