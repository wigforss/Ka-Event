package org.kasource.kaevent.event.dispatch;

import java.util.EventObject;

public interface EventRouter {

	public abstract void dispatchEvent(EventObject event, boolean blocked);

}