package org.kasource.kaevent.spring.bean;

import org.kasource.kaevent.core.bean.BeanResolver;
import org.kasource.kaevent.core.bean.CouldNotResolveBeanException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;



public class SpringBeanResolver implements BeanResolver, ApplicationContextAware{
	private ApplicationContext applicationContext;
	
	@Override
	public Object getBean(String beanName) {
		try {
			return applicationContext.getBean(beanName);
		} catch(Exception e) {
			throw new CouldNotResolveBeanException(e.getMessage(),e);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
		
	}

}
