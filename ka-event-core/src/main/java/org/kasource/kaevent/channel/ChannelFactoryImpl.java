package org.kasource.kaevent.channel;

import java.util.EventListener;
import java.util.EventObject;
import java.util.Set;

import javax.annotation.Resource;

import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.event.dispatch.EventMethodInvoker;
import org.kasource.kaevent.event.register.EventRegister;



/** 
 * Channels needs to be registered or created at this factory to be used.
 * 
 * @author rikard
 * @version $Id$
 **/
public class ChannelFactoryImpl implements ChannelFactory {
   
   private ChannelRegister channelRegister;

   private EventRegister eventRegister;
  
   private EventMethodInvoker eventMethodInvoker;
  
   private BeanResolver beanResolver;
   
   public ChannelFactoryImpl() {}
   
   public ChannelFactoryImpl(ChannelRegister channelRegister,EventRegister eventRegister,EventMethodInvoker eventMethodInvoker, BeanResolver beanResolver) {
       this.channelRegister = channelRegister;
       this.eventRegister = eventRegister;
       this.eventMethodInvoker = eventMethodInvoker;
       this.beanResolver = beanResolver;
   }
   
   
 
public Channel createChannel(String channelName) {
       ChannelImpl channel = new ChannelImpl(channelName, channelRegister, eventRegister, eventMethodInvoker, beanResolver);
       channelRegister.registerChannel(channel);
       return channel;
   }
   

public Channel createChannel(String channelName, Set<Class<? extends EventObject>> events) {
       ChannelImpl channel = new ChannelImpl(channelName, channelRegister, eventRegister, eventMethodInvoker, beanResolver);
       channelRegister.registerChannel(channel);
       for(Class<? extends EventObject> eventClass : events) {
           channel.registerEvent(eventClass);
       }
      
       return channel;
   }
   

public Channel createChannel(String channelName, Set<Class<? extends EventObject>> events, Set<EventListener> listeners) {
       ChannelImpl channel = new ChannelImpl(channelName, channelRegister, eventRegister, eventMethodInvoker, beanResolver);
       for(Class<? extends EventObject> eventClass : events) {
           channel.registerEvent(eventClass);
       }
       for(EventListener listener : listeners) {
           channel.registerListener(listener);
       }
       channelRegister.registerChannel(channel);
       return channel;
   }
 
}