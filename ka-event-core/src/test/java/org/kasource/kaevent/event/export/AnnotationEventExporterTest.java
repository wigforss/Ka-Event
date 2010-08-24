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
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.TestedObject;

/**
 * @author rikardwigforss
 *
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class AnnotationEventExporterTest {
    


    
    

    
    @Mock
    private EventFactory eventFactory;
    
    @Mock
    private EventConfig eventConfig;
    
    @TestedObject
    private AnnotationEventExporter exporter = new AnnotationEventExporter(AnnotationEventExporterTest.class.getPackage().getName());
    
    
    @Test
    public void scanAnnotationTest() throws IOException {
        EasyMock.expect(eventFactory.newFromAnnotatedEventClass(SimpleEvent.class)).andReturn(eventConfig);
        EasyMockUnitils.replay();
        Set<EventConfig> eventSet = exporter.exportEvents(eventFactory);   
        assertEquals(1, eventSet.size());
    }
    
   
}
