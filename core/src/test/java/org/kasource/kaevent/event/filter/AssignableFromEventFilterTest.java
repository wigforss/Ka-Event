package org.kasource.kaevent.event.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.EventObject;

import javax.sound.sampled.LineEvent;
import javax.swing.event.ChangeEvent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.util.InjectionUtils;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class AssignableFromEventFilterTest {

    private AssignableFromEventFilter filter = new AssignableFromEventFilter(EventObject.class);
    
    
    @Test
    public void passTest() {
        ChangeEvent event = new ChangeEvent("test");
        assertTrue(filter.passEventFilter(event));
    }
    
    @Test
    public void notPassTest() {
        ChangeEvent event = new ChangeEvent("test");
        InjectionUtils.injectInto(LineEvent.class, filter, "eventClass");
        assertFalse(filter.passEventFilter(event));
    }
    
    @Test
    public void notEnabledTest() {
        ChangeEvent event = new ChangeEvent("test");
        filter.setEnabled(false);
        assertTrue(filter.passEventFilter(event));
        assertFalse(filter.isEnabled());
    }
}
