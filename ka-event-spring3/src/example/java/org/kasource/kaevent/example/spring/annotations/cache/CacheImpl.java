package org.kasource.kaevent.example.spring.annotations.cache;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.example.spring.annotations.cache.event.EvictionEvent;
import org.kasource.kaevent.example.spring.annotations.cache.event.PutEvent;
import org.springframework.stereotype.Component;

@Component("cache")
public class CacheImpl implements Cache {

    private Map<Object, Object> data = new HashMap<Object, Object>();
    
    @Resource
    private EventDispatcher eventDispatcher;
    
    
    
    @Override
    public Object get(Object key) {
        return data.get(key);
    }

    @Override
    public void put(Object key, Object value) {
        data.put(key, value);
        eventDispatcher.fire(new PutEvent(this));
    }

    @Override
    public void evict(Object key) {
      data.remove(key);
      eventDispatcher.fire(new EvictionEvent(this));
    }

    
    
}
