package org.kasource.kaevent.event;

import java.util.EventObject;

public class ForwardedApplicationEvent extends ForwardedEvent {
    private static final long serialVersionUID = 1L;

    public ForwardedApplicationEvent(EventObject event) {
        super(event);
    }

}
