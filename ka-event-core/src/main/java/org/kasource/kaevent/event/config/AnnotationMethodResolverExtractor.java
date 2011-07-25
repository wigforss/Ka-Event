/**
 * 
 */
package org.kasource.kaevent.event.config;

import java.util.EventListener;
import java.util.EventObject;

import org.kasource.kaevent.annotations.event.methodresolving.BeanMethodResolver;
import org.kasource.kaevent.annotations.event.methodresolving.FactoryMethodResolver;
import org.kasource.kaevent.annotations.event.methodresolving.MethodResolving;
import org.kasource.kaevent.bean.BeanResolver;
import org.kasource.kaevent.event.method.MethodResolver;
import org.kasource.kaevent.event.method.MethodResolverFactory;

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
            return MethodResolverFactory.newKeywordSwitchByAnnotation(event, listener);
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
        return MethodResolverFactory.getFromBean(beanResolver, beanMethodResolver.value());
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
        return MethodResolverFactory.getFromFactoryMethod(factoryClass, methodName, parameter);

    }

    /**
     * @param beanResolver the beanResolver to set
     */
    public void setBeanResolver(BeanResolver beanResolver) {
        this.beanResolver = beanResolver;
    }
}
