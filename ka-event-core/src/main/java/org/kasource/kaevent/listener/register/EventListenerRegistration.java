/**
 * 
 */
package org.kasource.kaevent.listener.register;

import java.util.EventListener;
import java.util.EventObject;
import java.util.List;

import org.kasource.kaevent.event.filter.EventFilter;

/**
 * @author Rikard Wigforss
 */
public class EventListenerRegistration {
    private EventListener listener;
    private List<EventFilter<EventObject>> filters;
    
    public EventListenerRegistration(EventListener listener, List<EventFilter<EventObject>> filters) {
        this.listener = listener;
        this.filters = filters;
    }

    /**
     * @return the listener
     */
    public EventListener getListener() {
        return listener;
    }

    
   

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof EventListenerRegistration) {
            return listener.equals(((EventListenerRegistration) obj).getListener());
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return listener.hashCode();
    }

    /**
     * @return the filters
     */
    public List<EventFilter<EventObject>> getFilters() {
        return filters;
    }
    
    
}
