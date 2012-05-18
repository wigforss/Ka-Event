package org.kasource.kaevent.spring.context.event;

import java.util.EventListener;

import org.springframework.context.event.ContextStoppedEvent;

public interface ContextStoppedListener extends EventListener {
    public void onContextStopped(ContextStoppedEvent event);
}
