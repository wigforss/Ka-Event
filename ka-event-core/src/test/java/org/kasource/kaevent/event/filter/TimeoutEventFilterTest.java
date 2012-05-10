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
public class TimeoutEventFilterTest {

    
    
    @TestedObject
    private TimeoutEventFilter filter = new TimeoutEventFilter(100);

    
    
    @Test
    public void passTest() {
        BaseEvent event = new MyBaseEvent("test");
        assertTrue(filter.passFilter(event));
    }
    
    @Test
    public void notPassTest() throws InterruptedException {
        BaseEvent event = new MyBaseEvent("test");
        InjectionUtils.injectInto(1, filter, "timeoutMillis");
        Thread.sleep(50);
        assertFalse(filter.passFilter(event));
    }
    
    @Test
    public void notEnabledTest() throws InterruptedException {
        BaseEvent event = new MyBaseEvent("test");
        filter.setEnabled(false);
        InjectionUtils.injectInto(1, filter, "timeoutMillis");
        Thread.sleep(50);
        assertTrue(filter.passFilter(event));
    }
    
    @Test
    public void handlesEventTest() {
        assertTrue(filter.handlesEvent().equals(BaseEvent.class));
    }
    
    
    @SuppressWarnings("serial")
    private static class MyBaseEvent extends BaseEvent {
        public MyBaseEvent(String source) {
            super(source);
        }
    }
    
    
}
