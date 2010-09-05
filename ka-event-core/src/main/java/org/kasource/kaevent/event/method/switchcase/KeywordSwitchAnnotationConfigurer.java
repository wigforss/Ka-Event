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

import org.kasource.commons.reflection.ReflectionUtils;
import org.kasource.kaevent.event.EventKeyword;
import org.kasource.kaevent.listener.interfaces.CustomCase;
import org.kasource.kaevent.listener.interfaces.DefaultListenerMethod;
import org.kasource.kaevent.listener.interfaces.KeywordCase;

/**
 * @author rikardwigforss
 *
 */
public class KeywordSwitchAnnotationConfigurer {
    
   
    private Class<? extends EventObject> eventClass;
    private Class<? extends EventListener> listenerClass;
    private KeywordSwitchMethodResolver resolver;
    
    public KeywordSwitchAnnotationConfigurer(KeywordSwitchMethodResolver resolver,
                                            Class<? extends EventObject> eventClass,
                                            Class<? extends EventListener> listenerClass) {
        this.resolver = resolver;
        this.eventClass = eventClass;
        this.listenerClass = listenerClass;
    }
    
    public void configure() {
        Set<Method> defaultMethods = ReflectionUtils.getDeclaredAnnotatedMethods(listenerClass, DefaultListenerMethod.class);
        if (defaultMethods.size() == 1) {
            ReflectionUtils.verifyMethodSignature(defaultMethods.iterator().next(), Void.TYPE, eventClass);
            resolver.defaultMethod = defaultMethods.iterator().next();
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

    private void setCaseMethods() {
        Map<String, Method> methodMap = new HashMap<String, Method>();
        Set<Method> caseMethods = ReflectionUtils.getDeclaredAnnotatedMethods(listenerClass, KeywordCase.class);
        for (Method method : caseMethods) {
            ReflectionUtils.verifyMethodSignature(method, Void.TYPE, eventClass);
            KeywordCase caseAnnotation = method.getAnnotation(KeywordCase.class);
            methodMap.put(caseAnnotation.value(), method);
        }
        resolver.methodMap.putAll(methodMap);
    }

    private void setKeywordMethod() {
        Method method = ReflectionUtils.getDeclaredAnnotatedMethod(eventClass, EventKeyword.class);
        if(method == null) {
            throw new IllegalStateException(eventClass+" must declare one method annotated with @EventKeyword!");
        }
        resolver.eventKeywordMethod = method;
    }

    private void setCustomKeywordMethods() {
        Map<String, Method> methodMap = new HashMap<String, Method>();
        Set<Method> customCaseMethods = ReflectionUtils.getDeclaredInheritlyAnnotatedMethods(listenerClass, CustomCase.class);
        for (Method method : customCaseMethods) {
            ReflectionUtils.verifyMethodSignature(method, Void.TYPE, eventClass);
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().getAnnotation(CustomCase.class) != null) {
                    Method valueMethod;
                    Object keyword;
                    try {
                        valueMethod = annotation.annotationType().getMethod("value");
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
            KeywordCase caseAnnotation = method.getAnnotation(KeywordCase.class);
            methodMap.put(caseAnnotation.value(), method);
        }
        resolver.methodMap.putAll(methodMap);
    }
}

