package org.kasource.kaevent.event.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kasource.kaevent.event.BaseEvent;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.inject.util.InjectionUtils;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class ThreadNameEventFilterTest {

    @TestedObject
    private ThreadNameEventFilter filter = new ThreadNameEventFilter(".*MyTest.*");
    
    
    @Test
    public void passTest() {
        BaseEvent event = new MyBaseEvent("test");
        String regExp = Thread.currentThread().getName();
        InjectionUtils.injectInto(regExp, filter, "regExp");
        assertTrue(filter.passFilter(event));
    }
    
    @Test
    public void notPassTest() {
        BaseEvent event = new MyBaseEvent("test");
        assertFalse(filter.passFilter(event));
    }
    
    @Test
    public void notEnabledTest() {
        BaseEvent event = new MyBaseEvent("test");
        filter.setEnabled(false);
        assertTrue(filter.passFilter(event));
        assertFalse(filter.isEnabled());
    }
    
    @SuppressWarnings("serial")
    private static class MyBaseEvent extends BaseEvent {
        public MyBaseEvent(String source) {
            super(source);
        }
    }
    
    @Test
    public void handlesEventTest() {
        assertTrue(filter.handlesEvent().equals(BaseEvent.class));
    }
}
