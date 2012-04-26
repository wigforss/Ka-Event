package org.kasource.kaevent.event.filter;

import java.util.EventObject;

import org.kasource.kaevent.event.BaseEvent;

public class TimeoutEventFilter extends AbstractControllableEventFilter<BaseEvent> {
    
    private long timeoutMillis;
    
    public TimeoutEventFilter(long timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }
    

    @Override
    protected boolean passEventFilter(BaseEvent event) {
        return (event.getCreationTimestamp() + timeoutMillis) >= System.currentTimeMillis();
    }

    @Override
    public boolean isApplicable(Class<? extends EventObject> eventClass) {
        return BaseEvent.class.isAssignableFrom(eventClass);
    }
}
