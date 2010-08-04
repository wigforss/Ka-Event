/**
 * 
 */
package org.kasource.kaevent.event.dispatch;

import java.util.EventObject;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;




/**
 * @author wigforss
 *
 */
public class ThreadPoolQueueExecutor extends ThreadPoolExecutor implements DispatcherQueueThread {
    
    private static final int DEFAULT_CORE_POOL_SIZE = 5;
    private static final int DEFAULT_MAXIMUM_POOL_SIZE=10;
    private static final long DEFAULT_KEEP_ALIVE_TIME = 5;
    private static final TimeUnit DEFAULT_KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    private static final int DEFAULT_QUEUE_CAPACITY = 1000;
    
    
    private EventSender eventSender;
  
    
    public ThreadPoolQueueExecutor(EventSender eventSender) {
        super(DEFAULT_CORE_POOL_SIZE, 
                DEFAULT_MAXIMUM_POOL_SIZE, 
                DEFAULT_KEEP_ALIVE_TIME, 
                DEFAULT_KEEP_ALIVE_TIME_UNIT, new LinkedBlockingDeque<Runnable>(DEFAULT_QUEUE_CAPACITY));
        this.eventSender = eventSender;
    }
    
    
    
    public void enqueue(EventObject event) {
        super.execute(new EventRunner(eventSender, event));
    }
    
    
 
    /**
     * Method invoked prior to executing the given Runnable in the
     * given thread.  This method is invoked by thread <tt>t</tt> that
     * will execute task <tt>r</tt>, and may be used to re-initialize
     * ThreadLocals, or to perform logging.
     *
     *
     * @param t the thread that will run task r.
     * @param r the task that will be executed.
     */
    protected void beforeExecute(Thread t, Runnable r) { 
        
    }

    /**
     * Method invoked upon completion of execution of the given Runnable.
     * This method is invoked by the thread that executed the task. If
     * non-null, the Throwable is the uncaught <tt>RuntimeException</tt>
     * or <tt>Error</tt> that caused execution to terminate abruptly.
     *
     * <p><b>Note:</b> When actions are enclosed in tasks (such as
     * {@link FutureTask}) either explicitly or via methods such as
     * <tt>submit</tt>, these task objects catch and maintain
     * computational exceptions, and so they do not cause abrupt
     * termination, and the internal exceptions are <em>not</em>
     * passed to this method.
     *
     *
     * @param r the runnable that has completed.
     * @param t the exception that caused termination, or null if
     * execution completed normally.
     */
    protected void afterExecute(Runnable r, Throwable t) { 
       
    }

    
    private static class EventRunner implements Runnable{

        private EventSender eventSender;
        private EventObject event;
        
        EventRunner(EventSender eventSender, EventObject event) {
            this.eventSender = eventSender;
            this.event = event;
        }
        
        
        @Override
        public void run() {
            eventSender.dispatchEvent(event, false);
        }


       
        
    }

  
    
    /**
     * Returns true if any events is in queue
     * 
     * @return true if any events is in queue
     **/
    public boolean hasQueuedEvents() {
        return !getQueue().isEmpty();
    }
    
    /**
     * Return true if any events are in queue or currently executing an event
     * 
     * @return true if any events in or currently executing an event.
     */
    public boolean hasUndispatchedEvents(){
        return hasQueuedEvents() || getActiveCount() > 0; 
    }
    
    
    
    
    /**
     * Close and shutdown the queue
     **/
    public  void close() {
        super.shutdown();
    }

}
