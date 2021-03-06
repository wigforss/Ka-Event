package org.kasource.kaevent.event.method;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.EventObject;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.kasource.commons.reflection.MethodFilterBuilder;
import org.kasource.commons.reflection.filter.methods.MethodFilter;
import org.kasource.commons.util.reflection.MethodUtils;

/**
 * Method resolver that resolves what method to invoke by inspecting annotated methods on the target Object.
 * 
 * 
 * @author rikardwi
 **/
public class AnnotatedMethodMethodResolver implements MethodResolver<EventObject>{

    private Class<? extends Annotation> targetAnnotation;
    private Map<Class<?>, Method> resolvedMethods = new ConcurrentHashMap<Class<?>, Method>();
    @SuppressWarnings("rawtypes")
    private MethodResolver fallbackMethodResolver;
    
    
    public AnnotatedMethodMethodResolver(Class<? extends Annotation> targetAnnotation, @SuppressWarnings("rawtypes") MethodResolver fallbackMethodResolver) {
        this.targetAnnotation = targetAnnotation;
        this.fallbackMethodResolver = fallbackMethodResolver;
    }
    
    /**
     * Resolve method by inspecting annotated methods on the target object. If no method
     * could be found the fall back method resolver will be invoked.
     * 
     * @param event     Event to find registered annotations for.
     * @param target    Target object to inspect.
     * 
     * @returns Annotated method on target found, else if not found the result of invoking the 
     * fall back method resolver.
     **/
    @SuppressWarnings("unchecked")
    @Override
    public Method resolveMethod(EventObject event, Object target) {
        Method method = resolvedMethods.get(target.getClass());
        if (method == null) {
            method = resolveAnnotatedMethod(event, target);  
            if(method != null) {
                resolvedMethods.put(target.getClass(), method);
            }
        }
        if(method == null) {
            if(fallbackMethodResolver != null) {
                return fallbackMethodResolver.resolveMethod(event, target);
            }
        }
        return method;
    }
    
    /**
     * Finds the annotated method on target that is registered for the event.
     * 
     * @param event     Event to find registered annotations for.
     * @param target    Target object to inspect.
     * 
     * @return The method on target that has a registered annotation or null if no such method could be found.
     **/
    private Method resolveAnnotatedMethod(EventObject event, Object target) {
       
        MethodFilter filter = new MethodFilterBuilder().annotated(targetAnnotation).parametersSuperType(event.getClass()).build();
        Set<Method> methods = MethodUtils.getMethods(target.getClass(), filter);
       
        if(methods.isEmpty()) {
            return null;
        } else {
            return methods.iterator().next();
        }
        
    }

    /**
     * @return the targetAnnotation
     */
    public Class<? extends Annotation> getTargetAnnotation() {
        return targetAnnotation;
    }
    
    
}
