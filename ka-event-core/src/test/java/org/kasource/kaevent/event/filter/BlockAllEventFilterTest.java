package org.kasource.kaevent.event.filter;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import javax.swing.event.ChangeEvent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.kaevent.event.BaseEvent;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class BlockAllEventFilterTest {
    
    @TestedObject
    private BlockAllEventFilter filter;
    
    @Test
    public void notPassTest() {
        ChangeEvent event = new ChangeEvent("test");
        assertFalse(filter.passFilter(event));
    }
    
    @Test
    public void notEnabledTest() {
        ChangeEvent event = new ChangeEvent("test");
        filter.setEnabled(false);
        assertTrue(filter.passFilter(event));
        assertFalse(filter.isEnabled());
    }
    
    @Test
    public void applicableTest() {
        assertTrue(filter.isApplicable(ChangeEvent.class));
    }
}
