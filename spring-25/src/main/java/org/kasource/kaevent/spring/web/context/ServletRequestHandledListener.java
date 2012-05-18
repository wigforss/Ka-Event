package org.kasource.kaevent.spring.web.context;

import java.util.EventListener;

import org.springframework.web.context.support.ServletRequestHandledEvent;


public interface ServletRequestHandledListener extends EventListener {
    public void onServletRequestHandled(ServletRequestHandledEvent event);
}
