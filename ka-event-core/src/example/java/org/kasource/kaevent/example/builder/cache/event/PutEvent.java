package org.kasource.kaevent.example.builder.cache.event;

import org.kasource.kaevent.annotations.event.Event;
import org.kasource.kaevent.example.builder.cache.Cache;


public class PutEvent extends CacheEvent {

    private static final long serialVersionUID = 1L;

    public PutEvent(Cache cache) {
        super(cache);
    }

}
