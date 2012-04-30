package org.kasource.kaevent.channel;

import java.util.EventObject;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.kasource.kaevent.event.filter.EventFilter;
import org.kasource.kaevent.event.register.EventRegister;


public class CdiEventChannel extends ChannelAdapter implements FilterableChannel {

    private ChannelFilterHandler filterHandler;
   
    
    @Inject
    private BeanManager beanManager;
    
    @Inject
    public CdiEventChannel(ChannelRegister channelRegister, EventRegister eventRegister) {
        super(channelRegister, eventRegister);
        filterHandler = new ChannelFilterHandler(eventRegister);
    }


    @Override
    public void fireEvent(EventObject event, boolean blocked) {
       if(filterHandler.filterEvent(event)) {
          beanManager.fireEvent(event);
       }
    }
    
    @Override
    public void registerEvent(Class<? extends EventObject> eventClass) {
        super.registerEvent(eventClass);
        
    }
    
    @Override
    public void unregisterEvent(Class<? extends EventObject> eventClass) {
        super.unregisterEvent(eventClass);
        filterHandler.unregisterFilterFor(eventClass);
        
    }
    
    @Override
    public boolean registerFilter(EventFilter<? extends EventObject> filter) {
        return filterHandler.registerFilter(filter);
    }


}
