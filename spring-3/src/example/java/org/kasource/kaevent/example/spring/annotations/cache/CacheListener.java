package org.kasource.kaevent.example.spring.annotations.cache;

import org.kasource.kaevent.annotations.listener.BeanListener;
import org.kasource.kaevent.example.spring.annotations.cache.event.CacheEvent;
import org.kasource.kaevent.example.spring.annotations.cache.event.OnEviction;
import org.kasource.kaevent.example.spring.annotations.cache.event.OnPut;

import org.springframework.stereotype.Component;

@BeanListener("cache")
@Component
public class CacheListener {

    @OnEviction
    @OnPut
    public void handleCacheEvent(CacheEvent event) {
        System.out.println(event.getClass());
    }
}
