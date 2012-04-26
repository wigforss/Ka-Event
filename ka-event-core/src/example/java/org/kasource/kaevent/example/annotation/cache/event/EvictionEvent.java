package org.kasource.kaevent.example.annotation.cache.event;

import org.kasource.kaevent.annotations.event.Event;
import org.kasource.kaevent.example.annotation.cache.Cache;

@Event(annotation = OnEviction.class, eventQueue = "cacheEventQueue")
public class EvictionEvent extends CacheEvent {

    private static final long serialVersionUID = 1L;

    public EvictionEvent(Cache cache) {
        super(cache);
    }

}
