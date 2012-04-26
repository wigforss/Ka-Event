package org.kasource.kaevent.event.filter;

import java.util.EventObject;

public  abstract class AbstractControllableEventFilter<T extends EventObject> implements ControllableEventFilter<T> {

    private boolean enabled = true;
    
    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
    
    @Override
    public boolean passFilter(T event) {
        if(enabled) {
            return passEventFilter(event);
        }
        return true;
    }
    
    protected abstract boolean passEventFilter(T event);
    
    public boolean isApplicable(Class<? extends EventObject> eventClass) {
        return true;
    }
}
