package org.kasource.kaevent.bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Default implementation of BeanResolver.
 * 
 * 
 * @author rikardwi
 * @version $Id$
 **/
public class DefaultBeanResolver implements BeanResolver{

	private Map<String, Object> beanContext = new ConcurrentHashMap<String, Object>();
	
	/**
	 * Throws an exception since, plain Java SE does not have access
	 * to any managed beans.
	 **/
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getBean(String beanName, Class<T> ofType) {
		Object bean = beanContext.get(beanName);
		if(bean != null) {
			return (T) bean;
		}
	    throw new CouldNotResolveBeanException("No bean resolver set.");
	}
	
	public void addBean(String beanName, Object bean) {
		beanContext.put(beanName, bean);
	}
}
