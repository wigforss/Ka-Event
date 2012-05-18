package org.kasource.kaevent.example.annotation.cache;

import org.kasource.kaevent.example.annotation.cache.event.CacheEvent;
import org.kasource.kaevent.example.annotation.cache.event.OnEviction;
import org.kasource.kaevent.example.annotation.cache.event.OnPut;

public class CacheListener {

    @OnEviction
    @OnPut
    public void handleCacheEvent(CacheEvent event) {
        System.out.println(event.getClass());
    }
}
