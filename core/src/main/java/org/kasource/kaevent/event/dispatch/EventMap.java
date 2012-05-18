package org.kasource.kaevent.event.dispatch;

import java.util.EventObject;
import java.util.LinkedHashMap;
import java.util.Map;

public class EventMap extends LinkedHashMap<EventObject, Boolean>{
    private static final long serialVersionUID = 1L;
    
    private int maxEntries;

    
    public EventMap(int maxEntries) {
        super(maxEntries, 0.75F, false);
        this.maxEntries = maxEntries;
    }
    
    
    
    protected boolean removeEldestEntry(Map.Entry<EventObject, Boolean> eldest) {
        if(size() > maxEntries) {
            System.out.println(eldest.getValue());
        }
       return size() > maxEntries;
    }
    
    @Override
    public synchronized boolean containsKey(Object event) {
        return super.containsKey(event);
    }
    
    @Override
    public synchronized Boolean put(EventObject key, Boolean value) {
        return super.put(key, value);
    }
}
