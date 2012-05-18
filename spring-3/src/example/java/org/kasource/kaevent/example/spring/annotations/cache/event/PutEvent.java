package org.kasource.kaevent.example.spring.annotations.cache.event;

import org.kasource.kaevent.annotations.event.Event;
import org.kasource.kaevent.example.spring.annotations.cache.Cache;


@Event(annotation = OnPut.class, eventQueue = "cacheEventQueue")
public class PutEvent extends CacheEvent {

    private static final long serialVersionUID = 1L;

    public PutEvent(Cache cache) {
        super(cache);
    }

}
