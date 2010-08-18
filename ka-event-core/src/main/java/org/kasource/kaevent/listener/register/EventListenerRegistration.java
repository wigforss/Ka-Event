/**
 * 
 */
package org.kasource.kaevent.listener.register;

import java.util.EventListener;
import java.util.EventObject;
import java.util.List;
import java.util.Map;

import org.kasource.kaevent.event.filter.EventFilter;

/**
 * @author Rikard Wigforss
 */
public class EventListenerRegistration {
    private EventListener listener;
    private Map<Class<? extends EventObject>,List<EventFilter<EventObject>>> filtersByEventClass;
    
    public EventListenerRegistration(EventListener listener, Map<Class<? extends EventObject>,List<EventFilter<EventObject>>> filtersByEventClass) {
        this.listener = listener;
        this.filtersByEventClass = filtersByEventClass;
    }

    /**
     * @return the listener
     */
    public EventListener getListener() {
        return listener;
    }

    
    /**
     * @return the filters
     */
    public List<EventFilter<EventObject>> getFilters(Class<? extends EventObject> eventClass) {
        return filtersByEventClass != null ? filtersByEventClass.get(eventClass) : null;
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
    
    
}
