package org.kasource.kaevent.spring.context.event;

import java.util.EventListener;

import org.springframework.context.event.ContextRefreshedEvent;

public interface ContextRefreshedListener extends EventListener {
    public void onContextRefreshed(ContextRefreshedEvent event);
}
