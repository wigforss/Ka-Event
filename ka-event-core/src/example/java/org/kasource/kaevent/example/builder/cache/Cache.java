package org.kasource.kaevent.example.builder.cache;

public interface Cache {
    public Object get(Object key);
    
    public void put(Object key, Object value);
    
    public void evict(Object key);
}
