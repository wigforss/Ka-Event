package org.kasource.kaevent.spring.context.event;

import java.util.EventListener;

import org.springframework.context.event.ContextClosedEvent;

public interface ContextClosedListener extends EventListener {
    
    public void onContextClosed(ContextClosedEvent evet);
}
