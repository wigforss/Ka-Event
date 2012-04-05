package org.kasource.kaevent.event.method;

import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Map;

import org.kasource.commons.util.reflection.MethodUtils;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.bean.CouldNotResolveBeanException;
import org.kasource.kaevent.event.method.switchcase.KeywordSwitchMethodResolver;

/**
 * Method Resolver Factory.
 * 
 * @author rikardwigforss
 **/
public final class MethodResolverFactory {

    /**
     * Private Constructor.
     **/
    private MethodResolverFactory() {
    }

    /**
     * Returns a method resolver from a static factory method.
     * 
     * @param factoryClass  Factory Class
     * @param methodName    Static factory method.
     * @param parameter     Parameter to the static factory method.
     * 
     * @return Method Resolver form static factory method.
     * @throws IllegalStateException if the static factory method could not be invoked.
     **/
    @SuppressWarnings("rawtypes")
    public static MethodResolver getFromFactoryMethod(Class<?> factoryClass, String methodName, String parameter) 
        throws IllegalStateException {
        try {
            if (parameter == null || parameter.length() == 0) {
                Method method = MethodUtils.getDeclaredMethod(factoryClass, methodName);
                return (MethodResolver) method.invoke(null);
            } else {
                Method method = MethodUtils.getDeclaredMethod(factoryClass, methodName, String.class);
                return (MethodResolver) method.invoke(null, parameter);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Could not invoke factory method " + methodName + " on " + factoryClass);

        }
    }

   /**
    * Returns a method resolver from a bean resolver.
    * 
    * @param beanResolver   Bean Resolver to use.
    * @param beanName       Name of the bean.
    * 
    * @return Method resolver found by bean resolver. 
    * @throws CouldNotResolveBeanException if no bean named beanName of class MethodResolver can 
    * be found by the bean resolver.
    **/
    @SuppressWarnings("rawtypes")
    public static MethodResolver getFromBean(BeanResolver beanResolver, String beanName) 
        throws CouldNotResolveBeanException {
        
        return beanResolver.getBean(beanName, MethodResolver.class);
    }

    /**
     * Returns a MethodResolve by analyzing the annotations of eventClass and listenerClass.
     * 
     * @param eventClass     Event Class.
     * @param listenerClass  Event Listener Interface.
     * 
     * @return KeywordSwitchMethodResolver from annotations.
     **/
    @SuppressWarnings("rawtypes")
    public static MethodResolver newKeywordSwitchByAnnotation(Class<? extends EventObject> eventClass,
                Class<? extends EventListener> listenerClass) {
        return new KeywordSwitchMethodResolver(eventClass, listenerClass);
    }

    /**
     * Returns a MethodResolver by creating a new KeywordSwitchMethodResolver from parameters.
     * 
     * @param eventClass            Event Class.
     * @param listenerClass         Event Listener Interface
     * @param keywordMethodName     Name of the event keyword method (from Event Class). 
     * @param methodNameMap         Map of method names and keywords (from listenerClass).
     * @param defaultMethodName     Name of the default method (from listenerClass).
     * 
     * @return KeywordSwitchMethodResolver from parameters.
     **/
    @SuppressWarnings("rawtypes")
    public static MethodResolver newKeywordSwitch(Class<? extends EventObject> eventClass,
                Class<? extends EventListener> listenerClass, String keywordMethodName,
                Map<String, String> methodNameMap, String defaultMethodName) {

        return new KeywordSwitchMethodResolver(eventClass, listenerClass, defaultMethodName, methodNameMap,
                    keywordMethodName);
    }
}
