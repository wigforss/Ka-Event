package org.kasource.kaevent.inttest.spring.jms;

import java.util.EventObject;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.kasource.spring.jms.support.converter.hessian.HessianMessageConverter;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

public class EventMessageListener  {

    public boolean messageReceived;
    public String payload;
   
    public void onMessage(MyEvent event) {
        payload = event.getPayload();
        messageReceived = true; 
        
    }

    /**
     * @return the messageReceived
     */
    public boolean isMessageReceived() {
        return messageReceived;
    }

    /**
     * @param messageReceived the messageReceived to set
     */
    public void setMessageReceived(boolean messageReceived) {
        this.messageReceived = messageReceived;
    }

    /**
     * @return the source
     */
    public String getPayload() {
        return payload;
    }

    /**
     * @param source the source to set
     */
    public void setPayload(String payload) {
        this.payload = payload;
    }

}
