package org.kasource.kaevent.channel;

import java.lang.reflect.Constructor;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Set;


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
	   return createChannel(ChannelImpl.class, channelName);
   }
   
   public Channel createChannel(Class<? extends Channel> channelClass, String channelName) {
	Channel channel = getNewChannel(channelClass, channelName);
	channelRegister.registerChannel(channel);
       return channel;
   }
   

   public Channel createChannel(Class<? extends Channel> channelClass, String channelName, Set<Class<? extends EventObject>> events) {
	 Channel channel = getNewChannel(channelClass, channelName);
       channelRegister.registerChannel(channel);
       for(Class<? extends EventObject> eventClass : events) {
           channel.registerEvent(eventClass);
       }
      
       return channel;
   }
   

   public Channel createChannel(Class<? extends Channel> channelClass, String channelName, Set<Class<? extends EventObject>> events, Set<EventListener> listeners) {
       Channel channel = getNewChannel(channelClass, channelName);
       for(Class<? extends EventObject> eventClass : events) {
           channel.registerEvent(eventClass);
       }
       for(EventListener listener : listeners) {
           channel.registerListener(listener);
       }
       channelRegister.registerChannel(channel);
       return channel;
   }
 
	private  Channel getNewChannel(Class<? extends Channel> channelClass, String name) {
		try {
		if (ChannelImpl.class.isAssignableFrom(channelClass)) {
			
			Constructor<? extends Channel> cons = 
				channelClass.getConstructor(String.class, 
											ChannelRegister.class, 
											EventRegister.class, 
											EventMethodInvoker.class,
											BeanResolver.class);
			
			
			return cons.newInstance(name,channelRegister, eventRegister, eventMethodInvoker, beanResolver);
		} else {
			Channel channel =  channelClass.newInstance();
			channel.setName(name);
			return channel;
		}
		}catch(Exception e) {
			throw new IllegalStateException("Could not find or instanciate channel class " + channelClass, e);
		}
		
	}
}