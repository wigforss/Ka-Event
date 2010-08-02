package org.kasource.kaevent.bean;

public class CouldNotResolveBeanException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CouldNotResolveBeanException(Throwable t) {
		super(t);
	}
	
	public CouldNotResolveBeanException(String message) {
		super(message);
	}
	
	public CouldNotResolveBeanException(String message, Throwable t) {
		super(message, t);
	}
	
	
}
