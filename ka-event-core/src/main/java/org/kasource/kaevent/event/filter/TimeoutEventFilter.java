package org.kasource.kaevent.event.filter;



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
    public Class<BaseEvent> handlesEvent() {    
        return BaseEvent.class;
    }

   
}
