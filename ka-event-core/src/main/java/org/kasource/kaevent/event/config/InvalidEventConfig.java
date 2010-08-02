package org.kasource.kaevent.event.config;

import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.EventObject;



public class InvalidEventConfig implements EventConfig{

    private String errorMessage;
    private Throwable cause;
    
    public InvalidEventConfig(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public InvalidEventConfig(Throwable cause) {
        this.errorMessage = cause.getMessage();
        this.cause = cause;
    }
    
    
    
    
   

    @Override
    public Class<? extends EventObject> getEventClass() {
    	if(cause == null) {
    		throw new InvalidEventConfigurationException(errorMessage);
    	} else {
    		throw new InvalidEventConfigurationException(errorMessage, cause);
    	}
    }

    
 
    @Override
    public Method getEventMethod(EventObject event) {
    	if(cause == null) {
    		throw new InvalidEventConfigurationException(errorMessage);
    	} else {
    		throw new InvalidEventConfigurationException(errorMessage, cause);
    	}
    }

    @Override
    public Class<? extends EventListener> getListener() {
    	if(cause == null) {
    		throw new InvalidEventConfigurationException(errorMessage);
    	} else {
    		throw new InvalidEventConfigurationException(errorMessage, cause);
    	}
    }

 

    

	

	public String getErrorMessage() {
	    return errorMessage;
	}
	
}
