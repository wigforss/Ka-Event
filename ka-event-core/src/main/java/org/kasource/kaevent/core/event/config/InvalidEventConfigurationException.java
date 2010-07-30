package org.kasource.kaevent.core.event.config;

public class InvalidEventConfigurationException extends IllegalStateException {
	private static final long serialVersionUID = 1L;

	public InvalidEventConfigurationException(String message) {
		super(message);
	}
	
	public InvalidEventConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	
}
