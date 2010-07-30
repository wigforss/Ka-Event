package org.kasource.kaevent.core.event.config;

import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Map;



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
    public Method getEventMethod() {
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

    @Override
    public org.kasource.kaevent.core.event.method.MethodResolver<EventObject> getMethodResolver() {
    	if(cause == null) {
    		throw new InvalidEventConfigurationException(errorMessage);
    	} else {
    		throw new InvalidEventConfigurationException(errorMessage, cause);
    	}
    }

    

	@Override
	public Method getListenerMethod(String methodName) {
		if(cause == null) {
    		throw new InvalidEventConfigurationException(errorMessage);
    	} else {
    		throw new InvalidEventConfigurationException(errorMessage, cause);
    	}
	}

	@Override
	public String[] getChannels() {
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
