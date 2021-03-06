/**
 * 
 */
package org.kasource.kaevent.event.register;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.kaevent.annotations.event.Event;
import org.kasource.kaevent.event.config.EventBuilder;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.config.EventBuilderFactory;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;

/**
 * @author rikardwigforss
 *
 */
//CHECKSTYLE:OFF
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class DefaultEventRegisterTest {

    @Mock
    private EventConfig eventConfig;
    
    @Mock
    @InjectIntoByType
    private EventBuilderFactory eventBuilderFactory;
    
    @Mock
    private EventBuilder builder;
   
    @TestedObject
    private DefaultEventRegister register = new DefaultEventRegister(eventBuilderFactory);
    
    
    @SuppressWarnings("rawtypes")
    @Test
    public void registerEventTest() throws IOException {
        Set<EventConfig> importedEvents = new HashSet<EventConfig>();
        importedEvents.add(eventConfig);
        EasyMock.expect(eventBuilderFactory.getBuilder(ChangeEvent.class)).andReturn(builder);
        EasyMock.expect(builder.build()).andReturn(eventConfig);
        EasyMock.expect((Class) eventConfig.getEventClass()).andReturn(ChangeEvent.class);
        EasyMock.expect((Class) eventConfig.getListener()).andReturn(ChangeListener.class).times(3);
        EasyMock.expect((Class) eventConfig.getEventAnnotation()).andReturn(Event.class).times(2);
        EasyMock.expect(eventConfig.getName()).andReturn("Test event");
        EasyMockUnitils.replay();
        register.registerEvent(ChangeEvent.class);
        assertEquals(eventConfig, register.getEventByClass(ChangeEvent.class));
        assertEquals(eventConfig, register.getEventByInterface(ChangeListener.class));  
    }
    
    
}
