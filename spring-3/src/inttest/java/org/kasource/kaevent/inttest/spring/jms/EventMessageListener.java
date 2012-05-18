package org.kasource.kaevent.inttest.spring.jms;


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
     * @return the payload
     */
    public String getPayload() {
        return payload;
    }

    /**
     * @param payload the payload to set
     */
    public void setPayload(String payload) {
        this.payload = payload;
    }

    

}
