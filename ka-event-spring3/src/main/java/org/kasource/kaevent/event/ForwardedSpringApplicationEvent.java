package org.kasource.kaevent.event;

import java.util.EventObject;

public class ForwardedSpringApplicationEvent extends ForwardedEvent {
    private static final long serialVersionUID = 1L;

    public ForwardedSpringApplicationEvent(EventObject event) {
        super(event);
    }

}
