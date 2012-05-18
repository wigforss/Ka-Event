package org.kasource.kaevent.event.filter;

import java.util.EventObject;
import java.util.List;

public class EventFilterExecutor {

    /**
     * Return true if event passed the filters from eventRegistration.
     * 
     * @param filters
     *            List of Event filters.
     * @param event
     *            Event Object to test.
     * 
     * @return true if event passes filters, else false.
     */
    @SuppressWarnings("unchecked")
    public boolean passFilters(List<EventFilter<? extends EventObject>> filters, EventObject event) {
        if (filters != null) {
            boolean passes = true;
            for (EventFilter<? extends EventObject> filter : filters) {

                EventFilter<EventObject> eventFilter = (EventFilter<EventObject>) filter;
                if (eventFilter.handlesEvent().isAssignableFrom(event.getClass())) {
                    if (!eventFilter.passFilter(event)) {
                        passes = false;
                        break;
                    }
                }
            }
            return passes;
        }
        return true;
    }
}
