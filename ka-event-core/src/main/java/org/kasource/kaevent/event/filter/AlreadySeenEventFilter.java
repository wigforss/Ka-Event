package org.kasource.kaevent.event.filter;

import java.util.EventObject;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * An event filter that reject events that it has already seen.
 * 
 * This event filter is limited in memory to a fixed capacity and will 
 * throw away the oldest seen events when this capacity is reached.
 * 
 * Note that if serialization and deserialization has been done the same event will
 * might be regarded as different events if the default equals implementation has not been overriden.
 * 
 * @author rikardwi
 **/
public class AlreadySeenEventFilter extends AbstractControllableEventFilter<EventObject> {

    private final static int DEFAULT_MAX_SIZE = 100;
    
    private int maxSize = DEFAULT_MAX_SIZE;
    
    private Queue<EventObject> seenEvents = new ConcurrentLinkedQueue<EventObject>();
  
    
    @Override
    protected boolean passEventFilter(EventObject event) {
        if(!seenEvents.contains(event)) {
            seenEvents.offer(event);  
            if(seenEvents.size()  > maxSize) {
                seenEvents.poll();
            }
            return true;
        }
        return false;
    }

}
