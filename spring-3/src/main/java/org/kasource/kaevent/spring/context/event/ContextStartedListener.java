package org.kasource.kaevent.spring.context.event;

import java.util.EventListener;

import org.springframework.context.event.ContextStartedEvent;

public interface ContextStartedListener extends EventListener {
    public void onContextStarted(ContextStartedEvent event);
}
