package org.kasource.kaevent.channel;

import java.util.EventObject;

import org.kasource.kaevent.event.filter.EventFilter;
import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.event.register.NoSuchEventException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Channel that publish events as spring application events.
 * 
 * If this channel is used in an XML configuration scenario, a prototype bean needs
 * to be defined. Like:
 * <p>
 * <pre>
 * <bean id="springEventChannel" class="org.kasource.kaevent.channel.SpringEventChannel" scope="prototype">
 *      <constructor-arg ref="kaEvent.channelRegister"/>
        <constructor-arg ref="kaEvent.eventRegister"/>
 * </bean>
 * </pre>
 * 
 * @author rikardwi
 **/
@Component
@Scope("prototype")
public class SpringEventChannel extends ChannelAdapter implements ApplicationEventPublisherAware, FilterableChannel {

    private ApplicationEventPublisher eventPublisher;
    private ChannelFilterHandler filterHandler;
    
    @Autowired
    public SpringEventChannel(ChannelRegister channelRegister, EventRegister eventRegister) {
        super(channelRegister, eventRegister);
        filterHandler = new ChannelFilterHandler();
      
    }

    /**
     * Register a new Event type to the register.
     * 
     * @param eventClass
     *            New event type to handle
     *            
     * @throws NoSuchEventException if no event can be found of type eventClass.
     * @throws IllegalArgumentException if the eventClass does 
     *         not extend org.springframework.context.ApplicationEvent
     **/
    @Override
    public void registerEvent(Class<? extends EventObject> eventClass) throws IllegalArgumentException {
        if(ApplicationEvent.class.isAssignableFrom(eventClass)) {
            super.registerEvent(eventClass);
        } else {
            throw new IllegalArgumentException(SpringEventChannel.class.getName() 
                        + " can't register event " + eventClass.getName() + " it doesn't extend " 
                        + ApplicationEvent.class.getName());
        }
       
    }
    
    
    @Override
    public void unregisterEvent(Class<? extends EventObject> eventClass) {
        super.unregisterEvent(eventClass);
    }
    
    @Override
    public void fireEvent(EventObject event, boolean throwException) {
       if(filterHandler.filterEvent(event)) {
           eventPublisher.publishEvent((ApplicationEvent) event);
       }
        
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void registerFilter(EventFilter<? extends EventObject> filter) {
         filterHandler.registerFilter(filter);
    }
    

}
