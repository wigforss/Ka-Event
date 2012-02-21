package org.kasource.kaevent.example.annotation.cache.event;

import org.kasource.kaevent.annotations.event.Event;
import org.kasource.kaevent.example.annotation.cache.Cache;

@Event(annotation = OnPut.class)
public class PutEvent extends CacheEvent {

    private static final long serialVersionUID = 1L;

    public PutEvent(Cache cache) {
        super(cache);
    }

}
