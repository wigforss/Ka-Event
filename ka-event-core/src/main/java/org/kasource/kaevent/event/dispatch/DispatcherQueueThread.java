/**
 * 
 */
package org.kasource.kaevent.event.dispatch;

import java.util.EventObject;

/**
 * Event Queue Interface.
 * 
 * @author rikardwi
 **/
public interface DispatcherQueueThread {
    
    /**
     * Enqueue event to the Event Queue.
     * 
     * @param event Event object.
     **/
    public void enqueue(EventObject event);
    
    /**
     * Returns true if any events is in queue.
     * 
     * @return true if any events is in queue
     **/
    public boolean hasQueuedEvents();
    
    /**
     * Return true if any events are in queue or currently executing an event.
     * 
     * @return true if any events in or currently executing an event.
     */
    public boolean hasUndispatchedEvents();
    
    /**
     * Set the true to enable concurrent event dispatching false for 
     * dispatching events in strict sequence.
     * 
     * @param concurrent true if queue can use multiple threads to dispatch events.
     */
    public void setConcurrent(boolean concurrent);
    
    /**
     * Returns true if the queue uses multiple threads to dispatch events, else false.
     * 
     * @return concurrent.
     **/
    public boolean isConcurrent();
}
