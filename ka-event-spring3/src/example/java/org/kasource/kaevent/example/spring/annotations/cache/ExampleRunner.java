package org.kasource.kaevent.example.spring.annotations.cache;

import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.event.dispatch.DefaultEventDispatcher;
import org.kasource.kaevent.example.spring.annotations.channel.Thermometer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
        ApplicationContext context = 
            new ClassPathXmlApplicationContext("org/kasource/kaevent/example/spring/annotations/cache/cache-context.xml");
        Cache cache = (Cache) context.getBean("cache");
        cache.put("A", "A");
        cache.put("B", "B");
        cache.evict("A");
        

    }

}
