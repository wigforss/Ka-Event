package org.kasource.kaevent.example.spring.annotations.cache.event;

import org.kasource.kaevent.annotations.event.Event;
import org.kasource.kaevent.example.spring.annotations.cache.Cache;

@Event(annotation = OnEviction.class, eventQueue = "cacheEventQueue")
public class EvictionEvent extends CacheEvent {

    private static final long serialVersionUID = 1L;

    public EvictionEvent(Cache cache) {
        super(cache);
    }

}
