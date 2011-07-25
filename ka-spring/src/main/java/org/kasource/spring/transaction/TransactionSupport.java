package org.kasource.spring.transaction;

public interface TransactionSupport {



	
	
	/**
	 * Registers listener so that it can get notification of current transaction
	 * 
	 * @param listener	listener to register
	 **/
	public void addListener(TransactionListener listener);
	
	/**
	 * Remove listener form listening to current transaction
	 * 
	 * @param listener	listener to remove
	 **/
	public void removeListener(TransactionListener listener);
	
	

}