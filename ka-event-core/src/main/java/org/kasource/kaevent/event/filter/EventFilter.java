package org.kasource.kaevent.event.filter;

import java.util.EventObject;

public interface EventFilter<T extends EventObject> {

	public boolean passFilter(T event);
}
