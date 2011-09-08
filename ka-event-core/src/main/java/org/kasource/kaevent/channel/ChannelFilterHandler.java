package org.kasource.kaevent.channel;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.filter.EventFilter;
import org.kasource.kaevent.event.register.EventRegister;

public class ChannelFilterHandler {

    private EventRegister eventRegister;
     
    private Map<Class<? extends EventObject>, Collection<EventFilter<EventObject>>> filtersByEvent = 
        new HashMap<Class<? extends EventObject>, Collection<EventFilter<EventObject>>>();
    
    
    /**
     * Constructor.
     * 
     * @param eventRegister Event Register to use.
     */
    public ChannelFilterHandler(EventRegister eventRegister) {
        this.eventRegister = eventRegister;
    }
    
    /**
     * Filters events with filter list.
     * 
     * @param event Event to apply filters for. 
     * 
     * @return
     **/
    public boolean filterEvent(EventObject event) {
        Collection<EventFilter<EventObject>> filters = filtersByEvent.get(event.getClass());
        if (filters != null) {
            boolean passFilter = true;
            for (EventFilter<EventObject> filter : filters) {
                if (!filter.passFilter(event)) {
                    passFilter = false;
                    break;
                }
            }
            if (passFilter) {
                return true;
            }
        } else {
           return true;
        }
        return false;
    }
    
    
    /**
     * Removes any filters for eventClass
     * 
     * @param eventClass Event Class to remove filters for.
     **/
    public void unregisterFilterFor(Class<? extends EventObject> eventClass) {
        filtersByEvent.remove(eventClass);
    }
    
    /**
     * Register a filter which will invoked on all events routed
     * to this channel. Returns true if filter added else false.
     * 
     * Will only add the filter if it is relevant to the events registered
     * to the channel.
     * 
     * @param filter    Filter to add to the channel filters list.
     * 
     * @return Returns true if filter added else false.
     **/
    @SuppressWarnings("unchecked")
    public boolean registerFilter(EventFilter<EventObject> filter) {
        
        Class<? extends EventObject> eventClass = (Class<? extends EventObject>) ((ParameterizedType) filter
                .getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
        
        Collection<EventConfig> events = eventRegister.getEvents();
        boolean found = false;
        for (EventConfig eventConfig : events) {
            if (eventClass.isAssignableFrom(eventConfig.getEventClass())) {
                Collection<EventFilter<EventObject>> filters = filtersByEvent.get(eventClass);
                if (filters == null) {
                    filters = new ArrayList<EventFilter<EventObject>>();
                    filtersByEvent.put(eventConfig.getEventClass(), filters);
                }
                filters.add(filter);
                found = true;
            }
        }
        return found;
       
    }
}
