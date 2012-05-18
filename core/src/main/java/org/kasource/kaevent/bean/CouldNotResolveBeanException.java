package org.kasource.kaevent.bean;


/**
 * Exception thrown when bean could not be found by the BeanResolver.
 * 
 * @author rikardwi
 * @version $Id$
 **/
public class CouldNotResolveBeanException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @param t	Cause to this exception
	 **/
	public CouldNotResolveBeanException(Throwable t) {
		super(t);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param message	Error message
	 **/
	public CouldNotResolveBeanException(String message) {
		super(message);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param message	Error message
	 * @param t			Cause to this exception
	 **/
	public CouldNotResolveBeanException(String message, Throwable t) {
		super(message, t);
	}
	
	
}
