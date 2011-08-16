package org.kasource.kaevent.event.dispatch;

import java.util.EventObject;

/**
 * Routes an event to the correct destination.
 * 
 * @author rikardwi
 * @version $Id$
 **/
public interface EventRouter {

	/**
	 * Route the event to the correct destination.
	 * 
	 * @param event		Event to route.
	 * @param blocked	true to invoke the method in a blocked fashion, else false.
	 **/
	public abstract void routeEvent(EventObject event, boolean blocked);

}
