package org.kasource.kaevent.event.register;

/**
 * Exception thrown when EventRegister can't find the requested event.
 * 
 * @author Rikard Wigforss
 **/
public class NoSuchEventException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @param message Error message.
	 **/
	public NoSuchEventException(String message) {
		super(message);
	}
}
