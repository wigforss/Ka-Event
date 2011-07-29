package org.kasource.kaevent.bean;


/**
 * Default implementation of BeanResolver.
 * 
 * Using this implementation will cause an exception be thrown.
 * 
 * @author rikardwi
 * @version $Id$
 **/
public class DefaultBeanResolver implements BeanResolver{

	
	
	/**
	 * Throws an exception since, plain Java SE does not have access
	 * to any managed beans.
	 **/
	@Override
	public <T> T getBean(String beanName, Class<T> ofType) {
	    throw new CouldNotResolveBeanException("No bean resolver set.");
	}
}
