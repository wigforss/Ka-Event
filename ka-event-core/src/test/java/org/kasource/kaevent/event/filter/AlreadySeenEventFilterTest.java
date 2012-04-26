package org.kasource.kaevent.event.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.swing.event.ChangeEvent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class AlreadySeenEventFilterTest {
    
    @TestedObject
    private AlreadySeenEventFilter filter;
    
    @Test
    public void passTest() {
        ChangeEvent event = new ChangeEvent("test");
        assertTrue(filter.passFilter(event));
    }
    
    @Test
    public void notPassTest() {
        ChangeEvent event = new ChangeEvent("test");
        assertTrue(filter.passFilter(event));
        assertFalse(filter.passFilter(event));
    }
    
    @Test
    public void notEnabledTest() {
        ChangeEvent event = new ChangeEvent("test");
        filter.setEnabled(false);
        assertTrue(filter.passFilter(event));
        assertTrue(filter.passFilter(event));
        assertFalse(filter.isEnabled());
    }
}
