package org.kasource.kaevent.core.bean;



public class DefaultBeanResolver implements BeanResolver{

	@Override
	public Object getBean(String beanName) {
		throw new CouldNotResolveBeanException("No bean resolver set.");
	}

}
