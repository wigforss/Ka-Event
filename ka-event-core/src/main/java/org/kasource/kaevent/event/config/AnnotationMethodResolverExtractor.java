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
 * @version $Id$
 **/
public class AnnotationMethodResolverExtractor {
  
    private BeanResolver beanResolver;
    
    /**
     * Constructor.
     * 
     * @param beanResolver BeanResolver to use.
     **/
    public AnnotationMethodResolverExtractor(BeanResolver beanResolver) {
        this.beanResolver = beanResolver;
    }
    
    /**
     * Returns the method resolver by inspecting the methodResolving parameter from
     * the listener instance.
     * 
     * @param event				Event class.	
     * @param listener		  	Listener to get method resolver from.
     * @param methodResolving 	Method resolver annotation from event interface class.
     **/
    @SuppressWarnings("rawtypes")
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
     * Returns MethodResolver from a the bean resolver for the listener parameter.
     * 
     * @param listener	Event Listener interface class.
     * 
     * @return the method resolver found using the bean resolver.
     */
    @SuppressWarnings("rawtypes")
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

    /**
     * Return MethodResolver by invoking a static factory method.
     * 
     * @param listener Event Listener interface class
     * 
     * @return The MethodResolver from factory.
     **/
    @SuppressWarnings("rawtypes")
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
