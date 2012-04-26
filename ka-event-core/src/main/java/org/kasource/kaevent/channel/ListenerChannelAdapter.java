package org.kasource.kaevent.channel;

import java.util.Collection;
import java.util.EventObject;
import java.util.List;

import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.event.filter.EventFilter;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.listener.register.ChannelListenerRegister;
import org.kasource.kaevent.listener.register.ChannelListenerRegisterImpl;
import org.kasource.kaevent.listener.register.EventListenerRegistration;

/**
 * Channel Adapter for channels that handles listeners.
 * 
 * Extend this class to inherit capabilities to register listeners.
 * 
 * @author rikardwi
 * @version $Id: $
 **/
public abstract class ListenerChannelAdapter extends ChannelAdapter implements ListenerChannel {

	private ChannelListenerRegister listenerRegister; 
	
	
	/**
	 * Constructor.
	 * 
	 * @param name             Name of the channel.
	 * @param channelRegister  Channel Register.
	 * @param eventRegister    Event Register.
	 * @param beanResolver     Bean Resolver.
	 **/
	public ListenerChannelAdapter(String name, 
	                              ChannelRegister channelRegister, 
	                              EventRegister eventRegister, 
	                              BeanResolver beanResolver) {
		super(name, channelRegister, eventRegister);
		listenerRegister = new ChannelListenerRegisterImpl(this, eventRegister, beanResolver);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param channelRegister  Channel Register.
     * @param eventRegister    Event Register.
     * @param beanResolver     Bean Resolver.
	 **/
	public ListenerChannelAdapter(ChannelRegister channelRegister, 
	                              EventRegister eventRegister, 
	                              BeanResolver beanResolver) {
		super(channelRegister, eventRegister);
		listenerRegister = new ChannelListenerRegisterImpl(this, eventRegister, beanResolver);
	}
	
	/**
     * Register a new listener object to this channel.
     * 
     * @param listener
     *            Listener object to register
     * @param filters
     *             The event filters to use for this listener.
     **/ 
    @Override
    public void registerListener(Object listener, List<EventFilter<? extends EventObject>> filters) {
        listenerRegister.registerListener(listener, filters);
    }
    
    /**
     * Register a new listener object to this channel.
     * 
     * @param listener
     *            Listener object to register
     **/
    @Override
    public void registerListener(Object listener) {
        listenerRegister.registerListener(listener);
    }

    /**
     * Remove a registered listener from this channel.
     * 
     * @param listener
     *            Listener object to unregister
     **/
    @Override
    public void unregisterListener(Object listener) {
        listenerRegister.unregisterListener(listener);

    }
    
    @Override
	public void unregisterEvent(Class<? extends EventObject> eventClass) {
    	super.unregisterEvent(eventClass);
    	
    	Collection<EventListenerRegistration> listeners = listenerRegister.getListenersByEvent(eventClass);
		 for (EventListenerRegistration listenerReg : listeners) {
			 listenerRegister.unregisterListener(listenerReg.getListener());
		 }
    }
    
    /**
     * Returns the channel listener register.
     * 
     * @return the channel listener register.
     **/
    protected ChannelListenerRegister getListenerRegister() {
		return listenerRegister;
	}

}
