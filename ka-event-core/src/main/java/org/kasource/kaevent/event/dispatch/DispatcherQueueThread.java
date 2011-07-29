/**
 * 
 */
package org.kasource.kaevent.event.dispatch;

import java.util.EventObject;

/**
 * @author rikardwigforss
 *
 **/
public interface DispatcherQueueThread {
    
    public void enqueue(EventObject event);
    
    /**
     * Returns true if any events is in queue
     * 
     * @return true if any events is in queue
     **/
    public boolean hasQueuedEvents();
    
    /**
     * Return true if any events are in queue or currently executing an event
     * 
     * @return true if any events in or currently executing an event.
     */
    public boolean hasUndispatchedEvents();
    
    /**
     * Set the true to enable concurrent event dispatching false for 
     * dispatching events in strict sequence.
     * 
     * @param concurrent
     */
    public void setConcurrent(boolean concurrent);
    
    public boolean isConcurrent();
}
