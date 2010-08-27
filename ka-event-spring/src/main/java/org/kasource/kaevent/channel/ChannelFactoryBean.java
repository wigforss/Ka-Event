/**
 * 
 */
package org.kasource.kaevent.channel;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.spring.xml.KaEventSpringBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Rikard Wigforss
 *
 */
public class ChannelFactoryBean implements FactoryBean, ApplicationContextAware{

  
    
    private String name;

    private List<String> events;
    
    private ApplicationContext applicationContext;
    
    @Override
    public Object getObject() throws Exception {
    	Channel channel = getChannel();
    	ChannelRegister channelRegister = (ChannelRegister) applicationContext.getBean(KaEventSpringBean.CHANNEL_REGISTER.getId());
    	channelRegister.registerChannel(channel);
    	return channel;
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
        	return channelFactory.createChannel(name);
        } else {
        	return channelFactory.createChannel(name, eventSet);
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

   
}
