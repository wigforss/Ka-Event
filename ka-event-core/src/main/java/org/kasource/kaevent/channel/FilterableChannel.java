package org.kasource.kaevent.channel;

import java.util.EventObject;

import org.kasource.kaevent.event.filter.EventFilter;

/**
 * Channel the filters all incoming events.
 * 
 * @author rikardwi
 **/
public interface FilterableChannel extends Channel{

	 /**
     * Register a filter which will invoked on all events routed
     * to this channel. Returns true if filter added else false.
     * 
     * Will only add the filter if it is relevant to the events registered
     * to the channel.
     * 
     * @param filter	Filter to add to the channel filters list.
     * 
     * @return Returns true if filter added else false.
     **/
    public boolean registerFilter(EventFilter<EventObject> filter);
}
