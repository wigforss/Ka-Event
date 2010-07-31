package org.kasource.kaevent.core.channel;

import java.util.EventListener;
import java.util.EventObject;
import java.util.Set;



/** 
 * Channels needs to be registered or created at this factory to be used.
 * 
 * @author rikard
 * @version $Id$
 **/
public class ChannelFactory {

   private ChannelRegister channelRegister;
   //private EventRegister eventRegister;
   
   public Channel createChannel(String channelName) {
       ChannelImpl channel = new ChannelImpl(channelName, channelRegister);
       //registerEvents(channel);
       channelRegister.registerChannel(channel);
       return channel;
   }
   
   public Channel createChannel(String channelName, Set<Class<? extends EventObject>> events) {
       ChannelImpl channel = new ChannelImpl(channelName, channelRegister);
       //registerEvents(channel);
       for(Class<? extends EventObject> eventClass : events) {
           channel.registerEvent(eventClass);
       }
       channelRegister.registerChannel(channel);
       return channel;
   }
   
   public Channel createChannel(String channelName, Set<Class<? extends EventObject>> events, Set<EventListener> listeners) {
       ChannelImpl channel = new ChannelImpl(channelName, channelRegister);
      // registerEvents(channel);
       for(Class<? extends EventObject> eventClass : events) {
           channel.registerEvent(eventClass);
       }
       for(EventListener listener : listeners) {
           channel.registerListener(listener);
       }
       channelRegister.registerChannel(channel);
       return channel;
   }
   /* TODO: Enable later
   private void registerEvents(Channel channel) {
       Collection<EventConfig> events = eventRegister.getEvents();
       for(EventConfig event : events) {
           if(event.getChannels() != null) {
               for(String channelName : event.getChannels()) {
                   if(channel.getName().equals(channelName)) {
                       channel.registerEvent(event.getEventClass());
                   }
               }
           }
       }
   }
   */
}