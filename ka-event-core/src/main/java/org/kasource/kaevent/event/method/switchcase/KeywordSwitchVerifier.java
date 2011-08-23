/**
 * 
 */
package org.kasource.kaevent.event.method.switchcase;

import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Map;

import org.kasource.commons.reflection.ReflectionUtils;

/**
 * Verifies a KeywordSwitchMethodResolver configuration.
 * 
 * @author wigforss
 **/
public class KeywordSwitchVerifier {

    /**
     * Verifies a KeywordSwitchMethodResolver configuration.
     * 
     * @param resolver          The resolve to verify,
     * @param eventClass        The Event Class
     * @param listenerClass     The Event Listener Interface. 
     * 
     * @throws IllegalArgumentException if a illegal configuration has been made.
     **/
    public void verify(KeywordSwitchMethodResolver resolver, Class<? extends EventObject> eventClass,
            Class<? extends EventListener> listenerClass) throws IllegalArgumentException {
        verifyListenerMethod(resolver.getDefaultMethod(), eventClass, listenerClass);
        verifyKeywordMethod(resolver.getEventKeywordMethod(), eventClass);
        ReflectionUtils.verifyMethodSignature(resolver.getDefaultMethod(), Void.TYPE, eventClass);
        verifyMethodMap(resolver.getMethodMap(), eventClass, listenerClass);
    }
    
    /**
     * Verifies a listener method.
     * 
     * @param method         Method to verify.
     * @param eventClass     Event Class
     * @param listenerClass  Event Listener Interface.
     * 
     * @throws IllegalArgumentException if the default method is not a void method taking
     * eventClass as the only parameter.
     **/
    private void verifyListenerMethod(Method method, Class<? extends EventObject> eventClass,
            Class<? extends EventListener> listenerClass) throws IllegalArgumentException {
        ReflectionUtils.verifyMethodSignature(method, Void.TYPE, eventClass);
        if (!method.getDeclaringClass().equals(listenerClass)) {
            throw new IllegalArgumentException("Event method " + method + " must be declared in "
                    + listenerClass);
        }
    }

    /**
     * Verifies the event keyword method.
     * 
     * @param method        Method to verify,
     * @param eventClass    Event Class.
     * 
     * @throws IllegalArgumentException if method is not declared by the Event Class, the 
     * method does not have a return value or if the method takes any parameters. 
     */
    private void verifyKeywordMethod(Method method, Class<? extends EventObject> eventClass) 
        throws IllegalArgumentException {
        
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

    /**
     * Verifies all methods in the method map.
     * 
     * @param methodMap     Method Map to verify.
     * @param eventClass    Event Class.
     * @param listenerClass Event Listener Interface.
     * 
     * @throws IllegalArgumentException  if the any method in the method map is not a void method taking
     * eventClass as the only parameter.
     */
    private void verifyMethodMap(Map<String, 
                                Method> methodMap, 
                                Class<? extends EventObject> eventClass, 
                                Class<? extends EventListener> listenerClass) throws IllegalArgumentException {
        for (Map.Entry<String, Method> entry : methodMap.entrySet()) {
            verifyListenerMethod(entry.getValue(), eventClass, listenerClass);
        }
    }
}
