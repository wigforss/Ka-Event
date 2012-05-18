package org.kasource.kaevent.example.builder.cache;

import org.kasource.kaevent.example.builder.cache.event.CacheEvent;
import org.kasource.kaevent.example.builder.cache.event.OnEviction;
import org.kasource.kaevent.example.builder.cache.event.OnPut;

public class CacheListener {

    @OnEviction
    @OnPut
    public void handleCacheEvent(CacheEvent event) {
        System.out.println(event.getClass());
    }
}
