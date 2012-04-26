package org.kasource.kaevent.event.dispatch;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventQueueRegisterImpl implements EventQueueRegister {

    private Map<String, DispatcherQueueThread> eventQueues = new ConcurrentHashMap<String, DispatcherQueueThread>();
    
    @Override
    public void registerEventQueue(String name, DispatcherQueueThread eventQueue) {
        eventQueues.put(name, eventQueue);
    }
    
    @Override
    public DispatcherQueueThread get(String name) {
        DispatcherQueueThread eventQueue = eventQueues.get(name);
        if(eventQueue == null) {
            throw new IllegalArgumentException("No event queue named " + name + " has is found!");
        }
        return eventQueue;
    }
}
