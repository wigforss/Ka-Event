package org.kasource.kaevent.event.dispatch;

import java.util.EventObject;

public interface EventSender {

	public abstract void dispatchEvent(EventObject event, boolean blocked);

}