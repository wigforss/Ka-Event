/**
 * 
 */
package org.kasource.kaevent.channel;

import java.util.EventListener;
import java.util.EventObject;
import java.util.Set;

/**
 * @author rikardwigforss
 *
 */
public interface ChannelFactory {

	 public abstract Channel createChannel(String channelName);
	
    public abstract Channel createChannel(Class<? extends Channel> channelClass,String channelName);

    public abstract Channel createChannel(Class<? extends Channel> channelClass,String channelName, Set<Class<? extends EventObject>> events);

    public abstract Channel createChannel(Class<? extends Channel> channelClass,String channelName, Set<Class<? extends EventObject>> events,
            Set<EventListener> listeners);

}