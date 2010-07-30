package org.kasource.kaevent.core.filter;

import java.util.EventObject;
import java.util.Set;

public interface EventFilterRegister {
	
	public void initialize();
	
	public<T extends EventObject> void registerFilter(EventFilter<T> filter);
	
	public Set<EventFilter<? extends EventObject>> getFilters(Class<? extends EventObject> eventClass);
}