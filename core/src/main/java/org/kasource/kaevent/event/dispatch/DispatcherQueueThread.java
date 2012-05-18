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
     * dispatching events in strict sequence. This will have the same effect as setting
     * maxThreads to 1.
     * 
     * @param concurrent true if queue can use multiple threads to dispatch events.
     */
    public void setConcurrent(boolean concurrent);
    
    /**
     * Set the event router, which is used to dispatch
     * events.
     * 
     * @param eventRouter Event Router
     **/
    public void setEventRouter(EventRouter eventRouter);
    
    /**
     * Returns true if the queue uses multiple threads to dispatch events, else false.
     * 
     * @return concurrent.
     **/
    public boolean isConcurrent();
    
    /**
     * @param maxPoolSize the maxPoolSize to set
     */
    public void setMaxThreads(int maxThreads);
    
    /**
     * @param corePoolSize the corePoolSize to set
     */
    public void setCoreThreads(int coreThreads);
    
    /**
     * @param keepAliveTime the keepAliveTime of worker threads in milliseconds.
     */
    public void setKeepAliveTime(long keepAliveTime);
}
