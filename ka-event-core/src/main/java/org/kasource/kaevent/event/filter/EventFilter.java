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
}
