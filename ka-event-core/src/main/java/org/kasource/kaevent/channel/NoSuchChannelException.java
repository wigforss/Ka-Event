package org.kasource.kaevent.channel;

/**
 * Exception thrown if a channel can not be found.
 * 
 * @author rikardwi
 * @version $Id$
 ***/
public class NoSuchChannelException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
	 * Constructor.
	 * 
	 * @param message	Error message
	 **/
    public NoSuchChannelException(String message) {
        super(message);
    }
    
    /**
	 * Constructor.
	 * 
	 * @param message	Error message
	 * @param cause		Cause to this exception
	 **/
    public NoSuchChannelException(String message, Throwable cause) {
        super(message);
    }
    
   
}
