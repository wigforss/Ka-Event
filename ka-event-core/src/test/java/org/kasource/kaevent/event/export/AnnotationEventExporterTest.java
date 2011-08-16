/**
 * 
 */
package org.kasource.kaevent.event.export;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.config.EventFactory;
import org.kasource.kaevent.event.export.test.event.SimpleEvent;
import org.kasource.kaevent.event.export.test.event2.SimpleEvent2;
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
public class AnnotationEventExporterTest {
    

    @Mock
    private EventFactory eventFactory;
    
    @Mock
    private EventConfig eventConfig;
    
    @Mock
    private EventConfig eventConfig2;
    
    @TestedObject
    private AnnotationEventExporter exporter = 
        new AnnotationEventExporter("org.kasource.kaevent.event.export.test.event, "
                    + "org.kasource.kaevent.event.export.test.event2");
    
    
    @Test
    public void scanAnnotationTest() throws IOException {
        EasyMock.expect(eventFactory.newFromAnnotatedEventClass(SimpleEvent.class)).andReturn(eventConfig);
        EasyMock.expect(eventFactory.newFromAnnotatedEventClass(SimpleEvent2.class)).andReturn(eventConfig2);
        EasyMockUnitils.replay();
        Set<EventConfig> eventSet = exporter.exportEvents(eventFactory);   
        assertEquals(2, eventSet.size());
    }
    
   
}
