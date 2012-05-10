package org.kasource.kaevent.event.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.EventObject;

import javax.swing.event.ChangeEvent;

import org.junit.Test;
import org.junit.runner.RunWith;
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
    public void handlesEventTest() {
        assertTrue(filter.handlesEvent().equals(EventObject.class));
    }
}
