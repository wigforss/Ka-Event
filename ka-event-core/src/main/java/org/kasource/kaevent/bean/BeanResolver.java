package org.kasource.kaevent.bean;

/**
 * Extension point can be implemented by any IoC container to allow access managed beans.
 * 
 * The default implementation provided does not have access to any beans, since
 * there are no managed context in plain Java SE.
 *
 * Some features like finding method resolver by bean, needs another implementation of 
 * this interface to work.
 * 
 * @author rikardwi
 * @version $Id$
 **/
public interface BeanResolver {


	/**
	 * Returns the bean of the correct type.
	 * 
	 * @param <T>		Class of bean.
	 * @param beanName	Name of the bean to return.
	 * @param ofType	Class of the bean to get.
	 * 
	 * @return The bean with matching bean name.
	 * @throws CouldNotResolveBeanException if bean can not be located.
	 **/
	public <T> T getBean(String beanName, Class<T> ofType) throws CouldNotResolveBeanException;
}
