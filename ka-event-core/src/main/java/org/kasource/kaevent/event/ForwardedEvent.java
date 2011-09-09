package org.kasource.kaevent.event;

import java.util.EventObject;

public abstract class  ForwardedEvent extends EventObject {
    private static final long serialVersionUID = 1L;

    public ForwardedEvent(EventObject event) {
        super(event);
    }
    
    @Override
    public EventObject getSource() {
        return (EventObject) super.getSource();
    }
}
