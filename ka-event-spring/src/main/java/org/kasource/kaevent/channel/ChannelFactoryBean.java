/**
 * 
 */
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
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;

/**
 * @author Rikard Wigforss
 *
 */
public class ChannelFactoryBean implements FactoryBean, ApplicationContextAware{

  
    
    private String name;

    private Class<? extends Channel> channelClass;
    
	private List<String> events;
    
    private ApplicationContext applicationContext;
    
    private List<EventListener> listeners;
    
    private Map<EventListener, List<EventFilter<EventObject>>> filterMap;
    
   


	@Override
    public Object getObject() throws Exception {
    	Channel channel = getChannel();
    	ChannelRegister channelRegister = (ChannelRegister) applicationContext.getBean(KaEventSpringBean.CHANNEL_REGISTER.getId());
    	channelRegister.registerChannel(channel);
    	if(listeners != null) {
    		if(channel instanceof ListenerChannel) {
    			for(EventListener listener : listeners) {
    				List<EventFilter<EventObject>> filters = getFilter(listener);
    				if(filters != null) {
    					((ListenerChannel) channel).registerListener(listener, filters);
    				} else {
    					((ListenerChannel) channel).registerListener(listener);
    				}
    			}
    		} else {
    			throw new IllegalArgumentException("listener registered to the channel "+ channel.getName() 
    					+ " which does not allow listeners to registered, does not implement ListenerChannel.");
    		}
    	}
    	return channel;
    }

	private List<EventFilter<EventObject>> getFilter(Object listener) {
		if(filterMap != null) {
			return filterMap.get(listener);
		}
		return null;
	}
    
    private Channel getChannel() {
    	ChannelFactory channelFactory = (ChannelFactory) applicationContext.getBean(KaEventSpringBean.CHANNEL_FACTORY.getId());
    	EventRegister eventRegister = (EventRegister) applicationContext.getBean(KaEventSpringBean.EVENT_REGISTER.getId());
        Set<Class<? extends EventObject>> eventSet = new HashSet<Class<? extends EventObject>>();
        if(events != null) {
        	for(String eventName : events) {
        		eventSet.add(eventRegister.getEventByName(eventName).getEventClass());
        	}
        }
        if(eventSet.isEmpty()) {
        	return channelFactory.createChannel(channelClass, name);
        } else {
        	return channelFactory.createChannel(channelClass, name, eventSet);
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

	public void setEvents(List<String> events) {
		this.events = events;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
		
	}


	public void setListeners(List<EventListener> listeners) {
		this.listeners = listeners;
	}

	public void setChannelClass(Class<? extends Channel> channelClass) {
		if(channelClass == null) {
			this.channelClass = ChannelImpl.class;
		} else {
			this.channelClass = channelClass;
		}
	}
	
	public void setFilterMap(Map<EventListener, List<EventFilter<EventObject>>> filterMap) {
		this.filterMap = filterMap;
	}
   
}
