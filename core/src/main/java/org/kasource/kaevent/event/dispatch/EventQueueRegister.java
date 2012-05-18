package org.kasource.kaevent.event.dispatch;

public interface EventQueueRegister {

    public void registerEventQueue(String name, DispatcherQueueThread eventQueue);

    public DispatcherQueueThread get(String name);

}
