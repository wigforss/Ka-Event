package org.kasource.kaevent.inttest.spring.jms;

import java.util.EventObject;


import org.kasource.kaevent.annotations.event.Event;
import org.kasource.spring.jms.support.converter.javabeans.SerializationConstructor;

@SerializationConstructor("source")
@Event(listener=MyListener.class)
public class MyEvent extends EventObject {
    private static final long serialVersionUID = 1L;

    private String payload;
    
 
    public MyEvent(String source) {
        super(source);
        this.payload = source;
    }

   
    public String getPayload() {
        return payload;
    }
    
}
