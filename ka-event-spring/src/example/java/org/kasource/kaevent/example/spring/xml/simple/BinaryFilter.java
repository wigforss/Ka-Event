package org.kasource.kaevent.example.spring.xml.simple;

import java.util.EventObject;

import org.kasource.kaevent.event.filter.EventFilter;

///CLOVER:OFF
//CHECKSTYLE:OFF
public class BinaryFilter implements EventFilter<EventObject> {

	private boolean allow;
	
	public BinaryFilter(boolean allow) {
		this.allow = allow;
	}
	
	@Override
	public boolean passFilter(EventObject event) {
		return allow;
	}

    @Override
    public boolean isApplicable(Class<? extends EventObject> eventClass) {
        return true;
    }

}
