package org.kasource.kaevent.event.config;

/**
 * Exception thrown if an event is not correctly configured.
 * 
 * @author rikardwi
 * @version $Id$
 **/
public class InvalidEventConfigurationException extends IllegalStateException {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @param message	Error message
	 **/
	public InvalidEventConfigurationException(String message) {
		super(message);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param message	Error message
	 * @param cause		Cause to this exception
	 **/
	public InvalidEventConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	
}
