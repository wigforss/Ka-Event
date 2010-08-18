package org.kasource.kaevent.bean;

import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.bean.CouldNotResolveBeanException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * Provides bean lookup capabilities by using the spring application context.
 * 
 * @author Rikard Wigforss
 **/
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
