package org.kasource.kaevent.channel;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.dispatch.EventMethodInvoker;
import org.kasource.kaevent.event.filter.EventFilter;
import org.kasource.kaevent.event.register.EventRegister;



/**
 * Default Channel Implementation.
 * 
 * Channels allows listeners to listen to all types of events (registered at the
 * channel) without knowing the events source.
 * 
 * When register a EventListener object to the channel, all event listener
 * interfaces that that class implements (which also are registered at the
 * channel) will be registered to listen to.
 * 
 * @author rikard
 * @version $Id$
 **/
public class ChannelImpl extends ListenerChannelAdapter  implements FilterableChannel, ListenerChannel {
   
    
    private Map<Class<? extends EventObject>, Collection<EventFilter<EventObject>>> filtersByEvent = 
        new HashMap<Class<? extends EventObject>, Collection<EventFilter<EventObject>>>();
    
    
 
    private EventMethodInvoker eventMethodInvoker;
    
    /**
     * Constructor, should only be invoked by the ChannelFactory.
     * 
     * @param name					Name of the channel.
     * @param channelRegister		Register of all channels.
     * @param eventRegister		 	Register of all events.
     * @param eventMethodInvoker	Helper class to invoke event listener method.
     * @param beanResolver			Bean resolver to use.
     **/
    public ChannelImpl(String name, 
                      ChannelRegister 
                      channelRegister, 
                      EventRegister eventRegister, 
                      EventMethodInvoker eventMethodInvoker,
                      BeanResolver beanResolver) {
    	super(name, channelRegister, eventRegister, beanResolver);
    	this.eventMethodInvoker = eventMethodInvoker;
    }

    
   

    
    /**
     * Fire event synchronously to all channel listeners.
     * 
     * @param event     Event to dispatch
     * @param blocked   true the event is fired synchronously, else false. 
     **/
    @Override
    public void fireEvent(EventObject event, boolean blocked) {
        Collection<EventFilter<EventObject>> filters = filtersByEvent.get(event.getClass());
        if (filters != null) {
            boolean passFilter = true;
            for (EventFilter<EventObject> filter : filters) {
                if (!filter.passFilter(event)) {
                    passFilter = false;
                    break;
                }
            }
            if (passFilter) {
                eventMethodInvoker.invokeEventMethod(event, getListenerRegister().getListenersByEvent(event), blocked);
            }
        } else {
            eventMethodInvoker.invokeEventMethod(event, getListenerRegister().getListenersByEvent(event), blocked);
        }
    }
    
   

    @Override
	public void unregisterEvent(Class<? extends EventObject> eventClass) {
    	super.unregisterEvent(eventClass);
    	filtersByEvent.remove(eventClass);
    }
  
    
    
    

    
    
    /**
     * Register a filter which will invoked on all events routed
     * to this channel. Returns true if filter added else false.
     * 
     * Will only add the filter if it is relevant to the events registered
     * to the channel.
     * 
     * @param filter	Filter to add to the channel filters list.
     * 
     * @return Returns true if filter added else false.
     **/
    @Override
    @SuppressWarnings("unchecked")
    public boolean registerFilter(EventFilter<EventObject> filter) {
        
        Class<? extends EventObject> eventClass = (Class<? extends EventObject>) ((ParameterizedType) filter
                .getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
        
        Collection<EventConfig> events = getEventRegister().getEvents();
        boolean found = false;
        for (EventConfig eventConfig : events) {
            if (eventClass.isAssignableFrom(eventConfig.getEventClass())) {
                Collection<EventFilter<EventObject>> filters = filtersByEvent.get(eventClass);
                if (filters == null) {
                    filters = new ArrayList<EventFilter<EventObject>>();
                    filtersByEvent.put(eventConfig.getEventClass(), filters);
                }
                filters.add(filter);
                found = true;
            }
        }
        return found;
       
    }



	
   
  

}
