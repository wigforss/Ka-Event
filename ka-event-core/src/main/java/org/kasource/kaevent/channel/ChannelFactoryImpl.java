package org.kasource.kaevent.channel;

import java.lang.reflect.Constructor;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Set;

import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.event.dispatch.EventMethodInvoker;
import org.kasource.kaevent.event.register.EventRegister;



/** 
 * The default implementation of ChannelFactory.
 * 
 * @author rikard
 * @version $Id$
 **/
public class ChannelFactoryImpl implements ChannelFactory {
   
   private ChannelRegister channelRegister;

   private EventRegister eventRegister;
  
   private EventMethodInvoker eventMethodInvoker;
  
   private BeanResolver beanResolver;
   
   /**
    * Constructor.
    **/
   public ChannelFactoryImpl() { }
   
   /**
    * Constructor.
    * 
    * @param channelRegister		Register of all channels.
    * @param eventRegister			Register of all events.
    * @param eventMethodInvoker		Helper class to invoke event methods on listeners.
    * @param beanResolver			Bean Resolver to use.
    **/
   public ChannelFactoryImpl(ChannelRegister channelRegister,
                             EventRegister eventRegister,
                             EventMethodInvoker eventMethodInvoker, 
                             BeanResolver beanResolver) {
       this.channelRegister = channelRegister;
       this.eventRegister = eventRegister;
       this.eventMethodInvoker = eventMethodInvoker;
       this.beanResolver = beanResolver;
   }
   
   /**
   * Creates and registers a new channel of the default implementation ChannelImpl.
   * 
   * @param channelName Name of the new channel
   * 
   * @return The newly created channel.
   **/
   public Channel createChannel(String channelName) {
	   return createChannel(ChannelImpl.class, channelName);
   }
   
   /**
	 * Creates and registers a new channel of the default implementation ChannelImpl.
	 * 
	 * @param channelClass	The channel implementation to use (default is ChannelImpl).
	 * @param channelName 	Name of the new channel
	 * 
	 * @return The newly created channel.
	 **/
   public Channel createChannel(Class<? extends Channel> channelClass, String channelName) {
	   Channel channel = getNewChannel(channelClass, channelName);
	   channelRegister.registerChannel(channel);
       return channel;
   }
   
   /**
	 * Creates and registers a new channel of the supplied channel class, registers 
	 * all events.
	 * 
	 * @param channelClass	The channel implementation to use (default is ChannelImpl).
	 * @param channelName 	Name of the new channel.
	 * @param events		Set of events to register at the channel.
	 * 
	 * @return The newly created channel.
	 **/
   public Channel createChannel(Class<? extends Channel> channelClass, 
                                String channelName, 
                                Set<Class<? extends EventObject>> events) {
	   Channel channel = getNewChannel(channelClass, channelName);
       channelRegister.registerChannel(channel);
       for (Class<? extends EventObject> eventClass : events) {
           channel.registerEvent(eventClass);
       }
      
       return channel;
   }
   
   /**
	 * Creates and registers a new channel of the supplied channel class, registers 
	 * all events.
	 * 
	 * @param channelClass	The channel implementation to use (default is ChannelImpl).
	 * @param channelName 	Name of the new channel.
	 * @param events		Set of events to register at the channel.
	 * @param listeners		Listeners to register at the channel.
	 * @return The newly created channel.
	 **/
   public ListenerChannel createChannel(Class<? extends ListenerChannel> channelClass, 
                                        String channelName, 
                                        Set<Class<? extends EventObject>> events, 
                                        Set<EventListener> listeners) {
	   ListenerChannel channel = (ListenerChannel) getNewChannel(channelClass, channelName);
       for (Class<? extends EventObject> eventClass : events) {
           channel.registerEvent(eventClass);
       }
       for (EventListener listener : listeners) {
           channel.registerListener(listener);
       }
       channelRegister.registerChannel(channel);
       return channel;
   }
 
   /**
    * Create the channel instance and sets its name.
    * 
    * If the channel class matches to constructor parameters of the default channel (ChannelImpl) that 
    * constructor is used, else the empty constructor is invoked.
    * 
    * @param channelClass	Channel implementation class to instanciate.
    * @param name			Name of the channel.
    * @return a new Channel instance.
    * 
    * @throws IllegalStateException if a channel with name already created or channelClass could not be instancieated.
    **/
	protected Channel getNewChannel(Class<? extends Channel> channelClass, 
	                                String name) throws IllegalStateException {
		try {
			channelRegister.getChannel(name);
			throw new IllegalStateException("Channel with name " + name + " has already been created.");
		} catch (NoSuchChannelException nsce) {
			try {
				if (ChannelImpl.class.isAssignableFrom(channelClass)) {
					
					Constructor<? extends Channel> cons = 
						channelClass.getConstructor(String.class, 
											ChannelRegister.class, 
											EventRegister.class, 
											EventMethodInvoker.class,
											BeanResolver.class);
					return cons.newInstance(name, 
					                        channelRegister, 
					                        eventRegister, 
					                        eventMethodInvoker, 
					                        beanResolver);
				} else {
					Channel channel =  channelClass.newInstance();
					channel.setName(name);
					return channel;
				}
			} catch (Exception e) {
				throw new IllegalStateException("Could not find or instanciate channel class " 
				            + channelClass, e);
			}
		}
	}
}
