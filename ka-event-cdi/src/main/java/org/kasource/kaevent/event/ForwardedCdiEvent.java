package org.kasource.kaevent.event;

import java.util.EventObject;

public class ForwardedCdiEvent extends ForwardedEvent {
    private static final long serialVersionUID = 1L;

    public ForwardedCdiEvent(EventObject event) {
        super(event);
    }

}
