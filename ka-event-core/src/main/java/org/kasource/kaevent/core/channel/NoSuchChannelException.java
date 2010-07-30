package org.kasource.kaevent.core.channel;

public class NoSuchChannelException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NoSuchChannelException(String message) {
        super(message);
    }
    
    public NoSuchChannelException(String message, Throwable cause) {
        super(message);
    }
    
   
}
