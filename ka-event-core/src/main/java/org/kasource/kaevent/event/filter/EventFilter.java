package org.kasource.kaevent.event.filter;

import java.util.EventObject;

/**
 * Filter for Events.
 *
 * @param <T> Any class that extends EventObject.
 * 
 * @author rikardwi
 * @version $Id$
 **/
public interface EventFilter<T extends EventObject> {

	/**
	 * Returns true if event passes the filter.
	 * 
	 * @param event	Event to apply filter on.
	 * 
	 * @return true if event passes the filter, else false.
	 **/
	public boolean passFilter(T event);
	
	/**
	 * Returns true if this event applicable for this filter.
	 * 
	 * @param event The eventClass class to inspect.
	 * 
	 * @return true if the event is applicable for this filter, else true.
	 **/
	public Class<T> handlesEvent();
}
