package org.kasource.kaevent.bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Default implementation of BeanResolver.
 * 
 * Extend this class and populate the bean context.
 * 
 * @author rikardwi
 * @version $Id$
 **/
public class DefaultBeanResolver implements BeanResolver {

	private Map<String, Object> beanContext = new ConcurrentHashMap<String, Object>();
	
	/**
	 * Looks up and returns beans from the bean context.
	 * 
	 * @param <T>       Type of the bean to return.
	 * @param beanName  Name of bean.
	 * @param ofType	Type of the bean.
	 * 
	 * @return bean from the bean context with matching name and type.
	 **/
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getBean(String beanName, Class<T> ofType) {
		Object bean = beanContext.get(beanName);
		if (bean != null) {
			return (T) bean;
		}
	    throw new CouldNotResolveBeanException("No bean resolver set.");
	}
	
	/**
	 * Adds a bean to the bean context.
	 * 
	 * @param beanName Name of the bean.
	 * @param bean     Object to add to the bean context.
	 **/
	public void addBean(String beanName, Object bean) {
		beanContext.put(beanName, bean);
	}
}
