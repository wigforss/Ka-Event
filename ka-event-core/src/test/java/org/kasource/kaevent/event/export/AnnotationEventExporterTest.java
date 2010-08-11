/**
 * 
 */
package org.kasource.kaevent.event.export;

import java.io.IOException;
import java.util.EventObject;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.config.EventConfigFactory;
import org.kasource.kaevent.event.export.test.event.SimpleEvent;
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
public class AnnotationEventExporterTest {
    


    private String scanPath = AnnotationEventExporterTest.class.getPackage().getName();
    

    
    @Mock
    private EventConfigFactory eventConfigFactory;
    
    @Mock
    private EventConfig eventConfig;
    
    @TestedObject
    private AnnotationEventExporter exporter = new AnnotationEventExporter();
    
    
    @Test
    public void scanAnnotationTest() throws IOException {
        EasyMock.expect(eventConfigFactory.createEventConfig(SimpleEvent.class)).andReturn(eventConfig);
        EasyMockUnitils.replay();
        Set<EventConfig> eventSet = exporter.exportEvents(eventConfigFactory, scanPath);   
        assertEquals(1, eventSet.size());
    }
    
   
}
