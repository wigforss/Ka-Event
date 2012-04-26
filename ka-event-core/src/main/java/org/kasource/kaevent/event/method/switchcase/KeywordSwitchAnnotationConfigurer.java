/**
 * 
 */
package org.kasource.kaevent.event.method.switchcase;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.kasource.commons.reflection.filter.methods.AnnotatedMethodFilter;
import org.kasource.commons.reflection.filter.methods.InheritlyAnnotatedMethodFilter;
import org.kasource.commons.reflection.filter.methods.MethodFilterBuilder;
import org.kasource.commons.util.reflection.MethodUtils;
import org.kasource.kaevent.annotations.event.EventKeyword;
import org.kasource.kaevent.annotations.event.methodresolving.CustomCase;
import org.kasource.kaevent.annotations.event.methodresolving.DefaultListenerMethod;
import org.kasource.kaevent.annotations.event.methodresolving.KeywordCase;

/**
 * Configures KeywordSwitchMethodResolver instances.
 * 
 * @author rikardwigforss
 **/
public class KeywordSwitchAnnotationConfigurer {
    
   
    private Class<? extends EventObject> eventClass;
    private Class<? extends EventListener> listenerClass;
    private KeywordSwitchMethodResolver resolver;
    
    /**
     * Constructor.
     * 
     * @param resolver      The resolver instance to configure.
     * @param eventClass    The Event class.
     * @param listenerClass The Event Listener Interface class.
     **/
    public KeywordSwitchAnnotationConfigurer(KeywordSwitchMethodResolver resolver,
                                            Class<? extends EventObject> eventClass,
                                            Class<? extends EventListener> listenerClass) {
        this.resolver = resolver;
        this.eventClass = eventClass;
        this.listenerClass = listenerClass;
    }
    
    /**
     * Configures the resolver instance.
     * 
     * @throws IllegalStateException if the listener class does not have a method annotated with 
     * @DefaultListenerMethod or more than one method annotated with @DefaultListenerMethod.
     * @throws IllegalArgumentException if the default method found is not a void method taking
     * the eventClass as the only parameter.
     **/
    public void configure() throws IllegalStateException, IllegalArgumentException {
        Set<Method> defaultMethods =
            MethodUtils.getDeclaredMethods(listenerClass, new MethodFilterBuilder().annotated(DefaultListenerMethod.class).build());
        if (defaultMethods.size() == 1) {
            MethodUtils.verifyMethodSignature(defaultMethods.iterator().next(), Void.TYPE, eventClass);
            resolver.setDefaultMethod(defaultMethods.iterator().next());
            setCaseMethods();
            setCustomKeywordMethods();
        } else if (defaultMethods.size() == 0) {
            throw new IllegalStateException("Keyword method resolving requires one method in " + listenerClass
                    + " to be annotated with @DefaultListenerMethod");
        } else {
            throw new IllegalStateException("More than one method in " + listenerClass
                    + " is annotated with @DefaultListenerMethod");
        }
        setKeywordMethod();
    }

    /**
     * Adds methods annotated with @KeywordCase to the methodMap.
     * 
     * @throws IllegalArgumentException if methods annotated with @KeywordCase is not a void method taking
     * the eventClass as the only parameter.
     **/
    private void setCaseMethods() throws IllegalArgumentException {
        Map<String, Method> methodMap = new HashMap<String, Method>();
        Set<Method> caseMethods = MethodUtils.getDeclaredMethods(listenerClass, new MethodFilterBuilder().annotated(KeywordCase.class).build());
        for (Method method : caseMethods) {
            MethodUtils.verifyMethodSignature(method, Void.TYPE, eventClass);
            KeywordCase caseAnnotation = method.getAnnotation(KeywordCase.class);
            methodMap.put(caseAnnotation.value(), method);
        }
        resolver.getMethodMap().putAll(methodMap);
    }

    /**
     * Set the event keyword method, by finding the method on the eventClass
     * which is annotated with @EventKeyword.
     * 
     * @throws IllegalStateException if the eventClass does not have any method annotated
     * with @KeywordMethod.
     **/
    private void setKeywordMethod() throws IllegalStateException {
        Set<Method> methods = MethodUtils.getDeclaredMethods(eventClass, new MethodFilterBuilder().annotated(EventKeyword.class).build());
        if (methods.isEmpty()) {
            throw new IllegalStateException(eventClass + " must declare one method annotated with @EventKeyword!");
        }
        resolver.setEventKeywordMethod(methods.iterator().next());
    }

    /**
     * Finds methods annotated with an annotation that is annotated with @CustomCase 
     * and add those to the method map.
     * 
     * @throws IllegalArgumentException if methods annotated with an annotation which is 
     * annotated with @CustomCase is not a void method taking
     * the eventClass as the only parameter.
     **/
    private void setCustomKeywordMethods() throws IllegalArgumentException {
        Map<String, Method> methodMap = new HashMap<String, Method>();
        Set<Method> customCaseMethods = 
            MethodUtils.getMethods(listenerClass, new InheritlyAnnotatedMethodFilter(CustomCase.class));
        
        for (Method method : customCaseMethods) {
            MethodUtils.verifyMethodSignature(method, Void.TYPE, eventClass);
            Annotation[] annotations = method.getAnnotations();
            setCustomAnnotatedKeywordMethod(methodMap, method, annotations);
           // KeywordCase caseAnnotation = method.getAnnotation(KeywordCase.class);
           // methodMap.put(caseAnnotation.value(), method);
        }
        resolver.getMethodMap().putAll(methodMap);
    }

    /**
     * Adds the method to the method map, by resolving the value
     * of the the method annotation annotated with @CustomCase.
     *   
     * @param methodMap     Method map 
     * @param method        Method
     * @param annotations   Annotations for method.
     * 
     * @throws IllegalStateException if the annotation that is 
     * annotated with @CustomCase does not have a value.
     **/
    private void setCustomAnnotatedKeywordMethod(Map<String, 
                                                 Method> methodMap, 
                                                 Method method, 
                                                 Annotation[] annotations) throws IllegalStateException {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().isAnnotationPresent(CustomCase.class)) {
              
                Object keyword;
                try {
                   Method valueMethod = annotation.annotationType().getMethod("value");
                   keyword = valueMethod.invoke(annotation);
                } catch (Exception e) {
                    throw new IllegalStateException(annotation.annotationType()
                            + " needs to have a value attribute", e);
                }

                if (keyword == null) {
                    throw new IllegalStateException(annotation.annotationType()
                            + " the value attribute must be set");
                }
                methodMap.put(keyword.toString(), method);
            }
        }
    }
}

