package org.kasource.kaevent.channel;

import java.util.Collection;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;

import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.event.filter.EventFilter;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.listener.register.ChannelListenerRegister;
import org.kasource.kaevent.listener.register.ChannelListenerRegisterImpl;
import org.kasource.kaevent.listener.register.EventListenerRegistration;

public abstract class ListenerChannelAdapter extends ChannelAdapter implements ListenerChannel{

	private ChannelListenerRegister listenerRegister; 
	
	
	
	public ListenerChannelAdapter(String name, ChannelRegister channelRegister, EventRegister eventRegister, BeanResolver beanResolver) {
		super(name, channelRegister, eventRegister);
		listenerRegister = new ChannelListenerRegisterImpl(this, eventRegister, beanResolver);
	}
	
	public ListenerChannelAdapter(ChannelRegister channelRegister, EventRegister eventRegister, BeanResolver beanResolver) {
		super(channelRegister, eventRegister);
		listenerRegister = new ChannelListenerRegisterImpl(this, eventRegister, beanResolver);
	}
	
	/**
     * Register a new listener object to this channel
     * 
     * @param listener
     *            Listener object to register
     **/ 
    @Override
    public void registerListener(EventListener listener,List<EventFilter<EventObject>> filters) {
        listenerRegister.registerListener(listener, filters);
    }
    
    /**
     * Register a new listener object to this channel
     * 
     * @param listener
     *            Listener object to register
     **/
    @Override
    public void registerListener(EventListener listener) {
        listenerRegister.registerListener(listener);
    }

    /**
     * Remove a registered listener from this channel
     * 
     * @param listener
     *            Listener object to unregister
     **/
    @Override
    public void unregisterListener(EventListener listener) {
        listenerRegister.unregisterListener(listener);

    }
    
    @Override
	public void unregisterEvent(Class<? extends EventObject> eventClass) {
    	super.unregisterEvent(eventClass);
    	
    	Collection<EventListenerRegistration> listeners = listenerRegister.getListenersByEvent(eventClass);
		 for(EventListenerRegistration listenerReg : listeners) {
			 listenerRegister.unregisterListener(listenerReg.getListener());
		 }
    }
    
    protected ChannelListenerRegister getListenerRegister() {
		return listenerRegister;
	}

}
