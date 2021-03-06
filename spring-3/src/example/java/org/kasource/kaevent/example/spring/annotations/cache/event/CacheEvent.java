package org.kasource.kaevent.example.spring.annotations.cache.event;

import java.util.EventObject;

import org.kasource.kaevent.example.spring.annotations.cache.Cache;

public class CacheEvent extends EventObject {

    private static final long serialVersionUID = 1L;

    public CacheEvent(Cache cache) {
        super(cache);
    }
    
    @Override
    public Cache getSource() {
        return (Cache) super.getSource();
    }

}
