/**
 * 
 */
package org.kasource.kaevent.core.event.config;

import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.EventObject;

import org.kasource.commons.util.ReflectionUtils;
import org.kasource.kaevent.core.bean.BeanResolver;
import org.kasource.kaevent.core.event.method.MethodResolver;
import org.kasource.kaevent.core.event.method.switchcase.KeywordSwitchMethodResolver;
import org.kasource.kaevent.core.listener.interfaces.BeanMethodResolver;
import org.kasource.kaevent.core.listener.interfaces.FactoryMethodResolver;
import org.kasource.kaevent.core.listener.interfaces.MethodResolving;

/**
 * Extracts a method resolver from an event (and listener) by inspecting the annotations
 * 
 * @author wigforss
 **/
public class AnnotationMethodResolverExtractor {
  
    private BeanResolver beanResolver;
    
    /**
     * @param event
     * @param listener
     * @param methodResolving
     */
    public MethodResolver<EventObject> getMethodResolver(Class<? extends EventObject> event,
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
    private MethodResolver<EventObject> getMethodResoverFromBeanContext(Class<? extends EventListener> listener) {
        BeanMethodResolver beanMethodResolver = listener.getAnnotation(BeanMethodResolver.class);
        if (beanMethodResolver == null) {
            throw new IllegalStateException(
                    "The listener "
                            + listener
                            + " annotated with @MethodResolving(MethodResolvingType.BEAN) must be annotated with @BeanMethodResolver");
        }
        return (MethodResolver<EventObject>) beanResolver.getBean(beanMethodResolver.value());
    }

    @SuppressWarnings("unchecked")
    private MethodResolver<EventObject> getMethodResolverFromFactoryMethod(Class<? extends EventListener> listener) {
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
            if (parameter == null) {
                Method method = ReflectionUtils.getMethod(factoryClass, methodName);
                return (MethodResolver<EventObject>) method.invoke(null);
            } else {
                Method method = ReflectionUtils.getMethod(factoryClass, methodName, String.class);
                return (MethodResolver<EventObject>) method.invoke(null, parameter);
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
