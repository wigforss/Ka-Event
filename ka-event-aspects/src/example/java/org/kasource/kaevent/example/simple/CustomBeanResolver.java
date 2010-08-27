/**
 * 
 */
package org.kasource.kaevent.example.simple;

import java.util.HashMap;
import java.util.Map;

import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.bean.CouldNotResolveBeanException;

/**
 * @author rikardwigforss
 * 
 */
public class CustomBeanResolver implements BeanResolver {

    Map<String, Object> beans = new HashMap<String, Object>();

    @Override
    public Object getBean(String beanName) {
        Object bean = beans.get(beanName);
        if(bean == null) {
             throw new CouldNotResolveBeanException("Could not find bean "+beanName);
        }
        return bean;
    }

    
    @Override
    public <T> T getBean(String beanName, Class<T> ofType) {
        Object bean = beans.get(beanName);
        if(bean == null) {
             throw new CouldNotResolveBeanException("Could not find bean "+beanName);
        }
        return (T) bean;
    }
    
    public void putBean(String beanName, Object bean) {
        beans.put(beanName, bean);
    }

}