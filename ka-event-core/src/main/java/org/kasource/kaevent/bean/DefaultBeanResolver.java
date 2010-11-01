package org.kasource.kaevent.bean;



public class DefaultBeanResolver implements BeanResolver{

	
	

	@Override
	public <T> T getBean(String beanName, Class<T> ofType) {
	    throw new CouldNotResolveBeanException("No bean resolver set.");
	}
}
