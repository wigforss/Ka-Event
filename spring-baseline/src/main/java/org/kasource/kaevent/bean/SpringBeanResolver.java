package org.kasource.kaevent.bean;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Provides bean lookup capabilities by using the spring application context.
 * 
 * @author Rikard Wigforss
 **/
@Component("beanResolver")
public class SpringBeanResolver implements BeanResolver, ApplicationContextAware {
    private ApplicationContext applicationContext;

    

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getBean(String beanName, Class<T> ofClass) {
        try {
            return (T) applicationContext.getBean(beanName);
        } catch (Exception e) {
            throw new CouldNotResolveBeanException(e.getMessage(), e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;

    }

}
