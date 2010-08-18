package org.kasource.kaevent.bean;

public interface BeanResolver {

	public Object getBean(String beanName);
	
	public <T> T getBean(String beanName, Class<T> ofType);
}
