package org.kasource.kaevent.spring.web.context;

import java.util.EventListener;

import org.springframework.web.context.support.RequestHandledEvent;

public interface RequestHandledListener extends EventListener {
    public void onRequestHandled(RequestHandledEvent event);
}
