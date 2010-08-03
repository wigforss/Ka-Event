package org.kasource.kaevent.channel;

import java.util.EventListener;
import java.util.EventObject;
import java.util.Set;

import org.kasource.kaevent.event.dispatch.EventMethodInvoker;
import org.kasource.kaevent.event.register.EventRegister;



/** 
 * Channels needs to be registered or created at this factory to be used.
 * 
 * @author rikard
 * @version $Id$
 **/
public class ChannelFactory {

   private ChannelRegister channelRegister;
   private EventRegister eventRegister;
   private EventMethodInvoker eventMethodInvoker;
   
   public ChannelFactory(ChannelRegister channelRegister,EventRegister eventRegister,EventMethodInvoker eventMethodInvoker) {
       this.channelRegister = channelRegister;
       this.eventRegister = eventRegister;
       this.eventMethodInvoker = eventMethodInvoker;
   }
   
   
   public Channel createChannel(String channelName) {
       ChannelImpl channel = new ChannelImpl(channelName, channelRegister, eventRegister, eventMethodInvoker);
       //registerEvents(channel);
       channelRegister.registerChannel(channel);
       return channel;
   }
   
   public Channel createChannel(String channelName, Set<Class<? extends EventObject>> events) {
       ChannelImpl channel = new ChannelImpl(channelName, channelRegister, eventRegister, eventMethodInvoker);
       //registerEvents(channel);
       for(Class<? extends EventObject> eventClass : events) {
           channel.registerEvent(eventClass);
       }
       channelRegister.registerChannel(channel);
       return channel;
   }
   
   public Channel createChannel(String channelName, Set<Class<? extends EventObject>> events, Set<EventListener> listeners) {
       ChannelImpl channel = new ChannelImpl(channelName, channelRegister, eventRegister, eventMethodInvoker);
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