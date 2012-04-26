package org.kasource.kaevent.event.filter;

import java.util.EventObject;

public class AssignableFromEventFilter extends AbstractControllableEventFilter<EventObject> {

    private Class<? extends EventObject> eventClass;
  
    
    public AssignableFromEventFilter(Class<? extends EventObject> eventClass) {
        this.eventClass = eventClass;
      
    }
    

    @Override
    protected boolean passEventFilter(EventObject event) {
        return eventClass.isAssignableFrom(event.getClass());
    }

}
