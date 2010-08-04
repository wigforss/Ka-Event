/**
 * 
 */
package org.kasource.kaevent.event;

import java.util.EventListener;
import java.util.EventObject;

import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.channel.ChannelRegister;

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
    
    public ChannelRegister getChannelRegister();
     
    public void registerListener(EventListener listener,
            Object sourceObject);
}
