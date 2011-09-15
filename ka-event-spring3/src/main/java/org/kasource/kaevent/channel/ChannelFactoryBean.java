package org.kasource.kaevent.channel;

import java.util.EventListener;
import java.util.EventObject;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.kasource.kaevent.event.filter.EventFilter;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.spring.xml.KaEventSpringBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Spring Channel Factory Bean.
 * 
 * @author Rikard Wigforss
 **/
public class ChannelFactoryBean implements FactoryBean<Channel>, ApplicationContextAware {

    private String name;

    private Class<? extends Channel> channelClass;
    
	private List<String> events;
    
    private ApplicationContext applicationContext;
    
    private List<EventListener> listeners;
    
    private Map<EventListener, List<EventFilter<EventObject>>> filterMap;
    
    private Channel channelRef;

	@Override
    public Channel getObject() throws Exception {
    	Channel channel = getChannel();
    	ChannelRegister channelRegister = 
    		(ChannelRegister) applicationContext.getBean(KaEventSpringBean.CHANNEL_REGISTER.getId());
    	channelRegister.registerChannel(channel);
    	if (listeners != null) {
    		if (channel instanceof ListenerChannel) {
    			for (EventListener listener : listeners) {
    			    registerListener(listener, channel);
    			}
    		} else {
    			throw new IllegalArgumentException("listener registered to the channel "
    					+ channel.getName() + " which does not allow listeners to registered, "
    					+ "does not implement ListenerChannel.");
    		}
    	}
    	return channel;
    }

	/**
	 * Register Listener to channel.
	 * 
	 * @param listener Listener to register.
	 * @param channel  Channel to register at.
	 **/
	private void registerListener(EventListener listener, Channel channel) {
	    List<EventFilter<EventObject>> filters = getFilter(listener);
        if (filters != null) {
            ((ListenerChannel) channel).registerListener(listener, filters);
        } else {
            ((ListenerChannel) channel).registerListener(listener);
        }
	}
	
	/**
	 * Returns the filters for listener.
	 * 
	 * @param listener Listener to look-up filters for.
	 * 
	 * @return List of filter for listener.
	 **/
	private List<EventFilter<EventObject>> getFilter(Object listener) {
		if (filterMap != null) {
			return filterMap.get(listener);
		}
		return null;
	}
    
	/**
	 * Creates and returns a channel instance.
	 * 
	 * @return new Channel instance.
	 **/
    private Channel getChannel() {
        
    	ChannelFactory channelFactory = 
    		(ChannelFactory) applicationContext.getBean(KaEventSpringBean.CHANNEL_FACTORY.getId());
    	EventRegister eventRegister = 
    		(EventRegister) applicationContext.getBean(KaEventSpringBean.EVENT_REGISTER.getId());
        Set<Class<? extends EventObject>> eventSet = new HashSet<Class<? extends EventObject>>();
        
        ChannelRegister channelRegister = (ChannelRegister) applicationContext.getBean(KaEventSpringBean.CHANNEL_REGISTER.getId());
        
        if (events != null) {
        	for (String eventName : events) {
        		eventSet.add(eventRegister.getEventByName(eventName).getEventClass());
        	}
        }
        if (eventSet.isEmpty()) {
            return createChannel(channelFactory, channelRegister, name);
        } else {
        	return createChannel(channelFactory, channelRegister, name, eventSet);
        }
    }
    
    private Channel createChannel(ChannelFactory channelFactory, ChannelRegister channelRegister, String name) {
        if(channelRef == null) {
            return channelFactory.createChannel(channelClass, name);
        } else {
            channelRef.setName(name);
            channelRegister.registerChannel(channelRef);
            return channelRef;
        }
    }
    
    private Channel createChannel(ChannelFactory channelFactory, ChannelRegister channelRegister, String name, Set<Class<? extends EventObject>> eventSet) {
        if(channelRef == null) {
            return channelFactory.createChannel(channelClass, name, eventSet);
        } else {         
            channelRef.setName(name);
            channelRegister.registerChannel(channelRef);
            for(Class<? extends EventObject> eventClass : eventSet) {
                channelRef.registerEvent(eventClass);
            }
            
            return channelRef;
        }
    }
    
    
    @Override
    public Class<?> getObjectType() {
        
        return Channel.class;
    }

    

    @Override
    public boolean isSingleton() {
        return false;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Events to be handles by this Channel.
     * 
     * @param events Events to handle.
     **/
	public void setEvents(List<String> events) {
		this.events = events;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
		
	}

	/**
	 * Listener to register to this Channel.
	 * 
	 * @param listeners Listeners to add.
	 **/
	public void setListeners(List<EventListener> listeners) {
		this.listeners = listeners;
	}

	/**
	 * Channel Class to create instance of.
	 * 
	 * @param channelClass Class of channel.
	 **/
	public void setChannelClass(Class<? extends Channel> channelClass) {
		if (channelClass == null) {
			this.channelClass = ChannelImpl.class;
		} else {
			this.channelClass = channelClass;
		}
	}
	
	/**
	 * Filters for listeners.
	 * 
	 * @param filterMap Map of all listener filters.
	 **/
	public void setFilterMap(Map<EventListener, List<EventFilter<EventObject>>> filterMap) {
		this.filterMap = filterMap;
	}

    /**
     * @param channelRef the channelRef to set
     */
    public void setChannelRef(Channel channelRef) {
        this.channelRef = channelRef;
    }
   
}
