/**
 * 
 */
package org.kasource.kaevent.event.method;

import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Map;

import org.kasource.commons.util.ReflectionUtils;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.event.method.switchcase.KeywordSwitchMethodResolver;

/**
 * @author rikardwigforss
 *
 */
public class MethodResolverFactory {

    @SuppressWarnings("unchecked")
    public static MethodResolver getFromFactoryMethod(Class<?> factoryClass,  String methodName, String parameter) {
        try {
            if (parameter == null || parameter.length() == 0) {
                Method method = ReflectionUtils.getDeclaredMethod(factoryClass, methodName);
                return (MethodResolver) method.invoke(null);
            } else {
                Method method = ReflectionUtils.getDeclaredMethod(factoryClass, methodName, String.class);
                return (MethodResolver) method.invoke(null, parameter);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Could not invoke factory method " + methodName + " on " + factoryClass);
                   
        }
    }
    
    @SuppressWarnings("unchecked")
    public static MethodResolver getFromBean(BeanResolver beanResolver, String beanName) {
        return (MethodResolver) beanResolver.getBean(beanName);
    }
    
    @SuppressWarnings("unchecked")
    public static MethodResolver newKeywordSwitchByAnnotation(Class<? extends EventObject> eventClass,
            Class<? extends EventListener> listenerClass) {
       return new KeywordSwitchMethodResolver(eventClass, listenerClass);
    }
    
    @SuppressWarnings("unchecked")
    public static MethodResolver newKeywordSwitch(Class<? extends EventObject> eventClass,
            Class<? extends EventListener> listenerClass, String keywordMethodName, Map<String, String> methodNameMap, String defaultMethodName) {
       return new KeywordSwitchMethodResolver(eventClass, listenerClass, defaultMethodName, methodNameMap,keywordMethodName);
    }
}
