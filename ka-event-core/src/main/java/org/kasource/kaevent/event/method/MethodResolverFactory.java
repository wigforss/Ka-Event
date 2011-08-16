/**
 * 
 */
package org.kasource.kaevent.event.method;

import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Map;

import org.kasource.commons.reflection.ReflectionUtils;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.event.method.switchcase.KeywordSwitchMethodResolver;

/**
 * @author rikardwigforss
 * 
 */
public final class MethodResolverFactory {

    private MethodResolverFactory() {
    }

    @SuppressWarnings("rawtypes")
    public static MethodResolver getFromFactoryMethod(Class<?> factoryClass, String methodName, String parameter) {
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

  
    @SuppressWarnings("rawtypes")
    public static MethodResolver getFromBean(BeanResolver beanResolver, String beanName) {
        return beanResolver.getBean(beanName, MethodResolver.class);
    }

    @SuppressWarnings("rawtypes")
    public static MethodResolver newKeywordSwitchByAnnotation(Class<? extends EventObject> eventClass,
                Class<? extends EventListener> listenerClass) {
        return new KeywordSwitchMethodResolver(eventClass, listenerClass);
    }

    @SuppressWarnings("rawtypes")
    public static MethodResolver newKeywordSwitch(Class<? extends EventObject> eventClass,
                Class<? extends EventListener> listenerClass, String keywordMethodName,
                Map<String, String> methodNameMap, String defaultMethodName) {

        return new KeywordSwitchMethodResolver(eventClass, listenerClass, defaultMethodName, methodNameMap,
                    keywordMethodName);
    }
}
