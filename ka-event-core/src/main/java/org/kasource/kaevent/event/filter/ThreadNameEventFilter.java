package org.kasource.kaevent.event.filter;

import java.util.EventObject;
import java.util.regex.Pattern;

import org.kasource.kaevent.event.BaseEvent;

public class ThreadNameEventFilter extends AbstractControllableEventFilter<BaseEvent> {

    private String regExp;
  
    
    public ThreadNameEventFilter(String threadNameRegExp) {
        this.regExp = threadNameRegExp;
    }
    
    @Override
    protected boolean passEventFilter(BaseEvent event) {
        return Pattern.matches(regExp, event.getCreatorThreadName());
    }

    @Override
    public boolean isApplicable(Class<? extends EventObject> eventClass) {
        return BaseEvent.class.isAssignableFrom(eventClass);
    }
}
