package org.kasource.kaevent.channel;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import org.kasource.kaevent.event.filter.EventFilter;
import org.kasource.kaevent.event.filter.EventFilterExecutor;


public class ChannelFilterHandler {

    
     
    private EventFilterExecutor filterExecutor = new EventFilterExecutor();
    

    private List<EventFilter<? extends EventObject>> filters;
    
   
    
    /**
     * Filters events with filter list.
     * 
     * @param event Event to apply filters for. 
     * 
     * @return
     **/
    public boolean filterEvent(EventObject event) {
        
         return filterExecutor.passFilters(filters, event);       
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

    public void registerFilter(EventFilter<? extends EventObject> filter) {
        if(filters == null) {
            filters = new ArrayList<EventFilter<? extends EventObject>>();
        }
        
        filters.add(filter);
      
    }
}
