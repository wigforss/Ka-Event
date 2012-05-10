package org.kasource.kaevent.event.filter;

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
    public Class<BaseEvent> handlesEvent() {    
        return BaseEvent.class;
    }
}
