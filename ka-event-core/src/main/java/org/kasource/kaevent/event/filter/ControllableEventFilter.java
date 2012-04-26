package org.kasource.kaevent.event.filter;

import java.util.EventObject;

public interface ControllableEventFilter<T extends EventObject> extends EventFilter<T> {

    public void setEnabled(boolean enable);
    
    public boolean isEnabled();
    
   
}
