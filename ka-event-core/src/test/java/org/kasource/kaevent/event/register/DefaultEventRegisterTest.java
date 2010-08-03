/**
 * 
 */
package org.kasource.kaevent.event.register;

import java.io.IOException;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Set;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.easymock.EasyMock;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.export.AnnotationEventExporter;
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
public class DefaultEventRegisterTest {

    @Mock
    private EventConfig eventConfig;
    
    @Mock
    @InjectIntoByType
    private AnnotationEventExporter eventExporter;
    
    @TestedObject
    private DefaultEventRegister register = new DefaultEventRegister();
    
    
    @SuppressWarnings("unchecked")
    @Test
    public void registerAnnotatedEventsTest() throws IOException {
        Set<EventConfig> importedEvents = new HashSet<EventConfig>();
        importedEvents.add(eventConfig);
        EasyMock.expect(eventExporter.exportEvents()).andReturn(importedEvents);
        EasyMock.expect((Class) eventConfig.getEventClass()).andReturn(ChangeEvent.class);
        EasyMock.expect((Class) eventConfig.getListener()).andReturn(ChangeListener.class);
      
        EasyMockUnitils.replay();
        register.initialize();
        assertEquals(eventConfig, register.getEventByClass(ChangeEvent.class));
        assertEquals(eventConfig, register.getEventByInterface(ChangeListener.class));  
    }
    
    
}
