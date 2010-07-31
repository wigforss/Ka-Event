/**
 * 
 */
package org.kasource.kaevent.core.event.method.switchcase;

import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Map;

import org.kasource.commons.util.ReflectionUtils;

/**
 * @author rikardwigforss
 *
 */
public class KeywordSwitchVerifier {

    public void verify(KeywordSwitchMethodResolver resolver, Class<? extends EventObject> eventClass,
            Class<? extends EventListener> listenerClass) {
        verifyListenerMethod(resolver.defaultMethod, eventClass, listenerClass);
        verifyKeywordMethod(resolver.eventKeywordMethod, eventClass);
        ReflectionUtils.verifyMethodSignature(resolver.defaultMethod, Void.TYPE, eventClass);
        verifyMethodMap(resolver.methodMap,eventClass, listenerClass);
    }
    
    private void verifyListenerMethod(Method method, Class<? extends EventObject> eventClass,
            Class<? extends EventListener> listenerClass) {
        ReflectionUtils.verifyMethodSignature(method, Void.TYPE, eventClass);
        if (!method.getDeclaringClass().equals(listenerClass)) {
            throw new IllegalArgumentException("Event method " + method + " must be declared in "
                    + listenerClass);
        }
    }

    private void verifyKeywordMethod(Method method, Class<? extends EventObject> eventClass) {
        if (!method.getDeclaringClass().equals(eventClass)) {
            throw new IllegalArgumentException(method + " must be declared in event class " + eventClass);
        }
        if (method.getReturnType().equals(Void.TYPE)) {
            throw new IllegalArgumentException(method + " must return an Object to be used as keyword event method!");
        }
        if (method.getParameterTypes().length != 0) {
            throw new IllegalArgumentException(method + " must not have any parameters!");
        }
    }

    private void verifyMethodMap(Map<String, Method> methodMap, Class<? extends EventObject> eventClass, Class<? extends EventListener> listenerClass) {
        for (Map.Entry<String, Method> entry : methodMap.entrySet()) {
            verifyListenerMethod(entry.getValue(), eventClass, listenerClass);
        }
    }
}
