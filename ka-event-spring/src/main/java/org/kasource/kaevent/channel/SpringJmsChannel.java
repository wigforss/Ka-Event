package org.kasource.kaevent.channel;

import java.util.EventObject;

import org.kasource.kaevent.event.filter.EventFilter;
import org.kasource.kaevent.event.register.EventRegister;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jms.core.JmsTemplate;


/**
 * Channel that sends events to the default destination of the JMS Template.
 * 
 * Uses the MessageConverter registered with the JMS Template.
 * 
 * <b>Note:</b> Most serializers  will not serialize the Event Source object since
 *  java.util.EventObject.source is a transient field. 
 *  
 *  Either choose a MessageConverter which does not honor the transient keyword or store the 
 *  event payload in another field.
 * 
 * @author rikardwi
 **/
public class SpringJmsChannel extends ChannelAdapter implements FilterableChannel {

    private ChannelFilterHandler filterHandler;
    
   
    private JmsTemplate jmsTemplate;
  
    public SpringJmsChannel(ChannelRegister channelRegister, EventRegister eventRegister) {
        super(channelRegister, eventRegister);
        filterHandler = new ChannelFilterHandler(eventRegister);
    }
    
    @Override
    public void unregisterEvent(Class<? extends EventObject> eventClass) {
        super.unregisterEvent(eventClass);
        filterHandler.unregisterFilterFor(eventClass);
    }
    
    @Override
    public void fireEvent(EventObject event, boolean throwException) {
       if(filterHandler.filterEvent(event)) {
           sendMessage(event);
       }
        
    }
    
    private void sendMessage(EventObject event) {
        jmsTemplate.convertAndSend(event);
       
    }

    @Override
    public boolean registerFilter(EventFilter<? extends EventObject> filter) {
        return filterHandler.registerFilter(filter);
    }

    /**
     * @param jmsTemplate the jmsTemplate to use.
     */
    @Required
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }
    
   
}
