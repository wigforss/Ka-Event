package org.kasource.kaevent.bean;

import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.bean.CouldNotResolveBeanException;

@ApplicationScoped
public class CdiBeanResolver implements BeanResolver {

	@Inject
	private BeanManager beanManager;

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getBean(String beanName, Class<T> ofType)
			throws CouldNotResolveBeanException {
		Set<Bean<?>> beans = beanManager.getBeans(beanName);
		for(Bean<?> bean : beans) {
			if(ofType.isAssignableFrom(bean.getBeanClass())) {			
				return (T) beanManager.getContext(bean.getScope()).get(bean);
			}
		}
		throw new CouldNotResolveBeanException("Could not find any bean named: " + beanName + " of class: " + ofType);
	}
	
	
}
