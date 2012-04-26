package org.kasource.kaevent.example.builder.cache;

import org.kasource.kaevent.config.KaEventConfigBuilder;
import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.event.dispatch.DefaultEventDispatcher;
import org.kasource.kaevent.example.builder.cache.event.EvictionEvent;
import org.kasource.kaevent.example.builder.cache.event.OnPut;
import org.kasource.kaevent.example.builder.cache.event.PutEvent;
import org.kasource.kaevent.example.builder.cache.event.OnEviction;

/**
 * Example that shows the the Listener (CacheListener) can share listener method
 * if events has a common super class.
 *  
 * @author rikardwi
 **/
public class ExampleRunner {

    /**
     * @param args
     */
    public static void main(String[] args) {
        KaEventConfigBuilder config = new KaEventConfigBuilder();
        config.addEventBoundToAnnotation(PutEvent.class, OnPut.class, "cacheEventQueue")
            .addEventBoundToAnnotation(EvictionEvent.class, OnEviction.class, "cacheEventQueue")
            .addEventQueue("cacheEventQueue", 1);
            
        EventDispatcher eventDispatcher = 
            new DefaultEventDispatcher(config.build());
        Cache cache = new CacheImpl(eventDispatcher);
        CacheListener listener = new CacheListener();
        eventDispatcher.registerListener(listener, cache);
        cache.put("A", "A");
        cache.put("B", "B");
        cache.evict("A");
        

    }

}
