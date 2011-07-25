/**
 * 
 */
package org.kasource.kaevent.event;

import java.util.EventListener;
import java.util.EventObject;
import java.util.List;

import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.config.KaEventConfiguration;
import org.kasource.kaevent.config.KaEventInitializer;
import org.kasource.kaevent.event.config.EventFactory;
import org.kasource.kaevent.event.filter.EventFilter;

/**
 * @author rikardwigforss
 *
 */
public interface EventDispatcher {
    /**
     * Fire event and queue it for later delivery and return
     * 
     * @param event
     *            Event to fire
     **/
    public void fire(EventObject event);

    
    /**
     * Fire event and block until all listeners have processed the event.
     * 
     * @param event
     *            Event to fire
     **/
    public void fireBlocked(EventObject event);
    
    /**
     * Fire event when the current thread's transaction is successfully committed.
     * 
     * NOTE: May not be implemented by all implementations
     * 
     * @param event
     *            Event to fire
     */
    public void fireOnCommit(EventObject event);
    
    public Channel createChannel(String channelName);
    
    public Channel getChannel(String channelName);
     
    public void registerListener(EventListener listener,
            Object sourceObject);
    
    public void registerListener(EventListener listener,
            Object sourceObject, List<EventFilter<EventObject>> filters);
    
    public void addToBatch(EventObject event);
    
    public void clearBatch();
    
    public void fireBatch();
   
    
}
