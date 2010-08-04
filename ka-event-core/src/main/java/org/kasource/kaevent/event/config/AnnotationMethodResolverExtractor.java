/**
 * 
 */
package org.kasource.kaevent.event.config;

import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.EventObject;

import org.kasource.commons.util.ReflectionUtils;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.event.method.MethodResolver;
import org.kasource.kaevent.event.method.switchcase.KeywordSwitchMethodResolver;
import org.kasource.kaevent.listener.interfaces.BeanMethodResolver;
import org.kasource.kaevent.listener.interfaces.FactoryMethodResolver;
import org.kasource.kaevent.listener.interfaces.MethodResolving;

/**
 * Extracts a method resolver from an event (and listener) by inspecting the annotations
 * 
 * @author wigforss
 **/
public class AnnotationMethodResolverExtractor {
  
    private BeanResolver beanResolver;
    
    
    public AnnotationMethodResolverExtractor(BeanResolver beanResolver) {
        this.beanResolver = beanResolver;
    }
    
    /**
     * @param event
     * @param listener
     * @param methodResolving
     */
    @SuppressWarnings("unchecked")
    public MethodResolver getMethodResolver(Class<? extends EventObject> event,
            Class<? extends EventListener> listener, MethodResolving methodResolving) {
        switch (methodResolving.value()) {
        case KEYWORD_SWITCH:
            return new KeywordSwitchMethodResolver(event, listener);
        case BEAN:
            return getMethodResoverFromBeanContext(listener);
        case FACTORY:
            return getMethodResolverFromFactoryMethod(listener);
        }
        return null;
    }

    /**
     * @param listener
     */
    @SuppressWarnings("unchecked")
    private MethodResolver getMethodResoverFromBeanContext(Class<? extends EventListener> listener) {
        BeanMethodResolver beanMethodResolver = listener.getAnnotation(BeanMethodResolver.class);
        if (beanMethodResolver == null) {
            throw new IllegalStateException(
                    "The listener "
                            + listener
                            + " annotated with @MethodResolving(MethodResolvingType.BEAN) must be annotated with @BeanMethodResolver");
        }
        return (MethodResolver) beanResolver.getBean(beanMethodResolver.value());
    }

    @SuppressWarnings("unchecked")
    private MethodResolver getMethodResolverFromFactoryMethod(Class<? extends EventListener> listener) {
        FactoryMethodResolver factoryMethodResolver = listener.getAnnotation(FactoryMethodResolver.class);
        if (factoryMethodResolver == null) {
            throw new IllegalStateException(
                    "The listener "
                            + listener
                            + " annotated with @MethodResolving(MethodResolvingType.FACTORY) must be annotated with @FactoryMethodResolver");
        }
        Class<?> factoryClass = factoryMethodResolver.factoryClass();
        String methodName = factoryMethodResolver.factoryMethod();
        String parameter = factoryMethodResolver.factoryMethodArgument();
        try {
            if (parameter == null || parameter.length() == 0) {
                Method method = ReflectionUtils.getDeclaredMethod(factoryClass, methodName);
                return (MethodResolver) method.invoke(null);
            } else {
                Method method = ReflectionUtils.getDeclaredMethod(factoryClass, methodName, String.class);
                return (MethodResolver) method.invoke(null, parameter);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Could not invoke factory method " + methodName + " on " + factoryClass
                    + " found in @FactoryMethodResolver of class " + listener);
        }
    }

    /**
     * @param beanResolver the beanResolver to set
     */
    public void setBeanResolver(BeanResolver beanResolver) {
        this.beanResolver = beanResolver;
    }
}
