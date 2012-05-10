package org.kasource.kaevent.example.spring.xml.channel;

import java.util.EventObject;

import org.kasource.kaevent.event.dispatch.ThreadPoolQueueExecutor;

public class MyEventQueueThread extends ThreadPoolQueueExecutor {

    public void enqueue(EventObject event){
        System.out.println(event + " queued");
        super.enqueue(event);
    }
}
