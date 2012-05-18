package org.kasource.kaevent.example.annotation.cache;

import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.event.dispatch.DefaultEventDispatcher;

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
        EventDispatcher eventDispatcher = 
            new DefaultEventDispatcher(ExampleRunner.class.getPackage().getName().replace('.', '/')
                        + "/cache-config.xml");
        Cache cache = new CacheImpl(eventDispatcher);
        CacheListener listener = new CacheListener();
        eventDispatcher.registerListener(listener, cache);
        cache.put("A", "A");
        cache.put("B", "B");
        cache.evict("A");
        

    }

}
