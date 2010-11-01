package org.kasource.kaevent.bean;

public interface BeanResolver {


	
	public <T> T getBean(String beanName, Class<T> ofType);
}
