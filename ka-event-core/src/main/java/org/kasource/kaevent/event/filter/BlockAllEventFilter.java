package org.kasource.kaevent.event.filter;

import java.util.EventObject;

/**
 * Blocks all events if enabled. Disable this filter to allow all.
 * 
 * @author rikardwi
 **/
public class BlockAllEventFilter extends AbstractControllableEventFilter<EventObject>{

    @Override
    protected boolean passEventFilter(EventObject event) {
       return false;
    }

    @Override
    public Class<EventObject> handlesEvent() {
        return EventObject.class;
    }

}
