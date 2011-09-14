package org.kasource.kaevent.inttest.spring.jms;

import java.util.EventListener;

public interface MyListener extends EventListener {

    public void onMyEvent(MyEvent event);
}
