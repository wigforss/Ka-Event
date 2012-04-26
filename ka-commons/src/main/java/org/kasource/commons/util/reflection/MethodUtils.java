package org.kasource.commons.util.reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.kasource.commons.reflection.filter.methods.MethodFilter;

/**
 * Utility class for Method based introspection of classes.
 * 
 * @author rikardwi
 **/
public class MethodUtils {
    
    private MethodUtils() {}
    
    /**
     * Returns the number of methods declared by a class <i>clazz</i>.
     */
    public static int getDeclaredMethodCount(Class<?> clazz) {
        return clazz.getDeclaredMethods().length;
    }

    /**
     * Returns the named method from class <i>clazz</i>.
     * 
     * @param clazz
     *            The class to inspect
     * @param name
     *            The name of the method to get
     * @param params
     *            Parameter types for the method
     * 
     * @throws IllegalArgumentException
     *             if method does not exists or cannot be accessed.
     * 
     * @return Returns the named method from class <i>clazz</i>.
     */
    public static Method getDeclaredMethod(Class<?> clazz, String name, Class<?>... params) {
        try {
            return clazz.getDeclaredMethod(name, params);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not access method: " + name + " on " + clazz, e);
        }
    }

    /**
     * Returns the methods declared by clazz which matches the supplied
     * method filter.
     * 
     * @param clazz
     *            The class to inspect
     * @param methodFilter
     *            The method filter to apply.
     * 
     * @return methods that match the params argument in their method signature
     * 
     **/
    public static Set<Method> getDeclaredMethods(Class<?> clazz, MethodFilter methodFilter) {
        Method[] methods = clazz.getDeclaredMethods();
        Set<Method> matches = new HashSet<Method>();
        for (Method method : methods) {       
            if (methodFilter.passFilter(method)) {
                matches.add(method);
            }
        }
        return matches;
    }

    /**
     * Returns the methods declared by clazz and any of its super classes, which matches the supplied
     * methodFilter.
     * 
     * @param clazz
     *            The class to inspect
     * @param methodFilter
     *            The method filter to apply.
     * 
     * @return methods that match the params argument in their method signature
     * 
     **/
    public static Set<Method> getMethods(Class<?> clazz, MethodFilter methodFilter) {
       
        Set<Method> matches = getDeclaredMethods(clazz, methodFilter);
        while((clazz = clazz.getSuperclass()) != null) {     
            matches.addAll(getDeclaredMethods(clazz, methodFilter));
        }
        return matches;
    }
      

    /**
     * Returns true if method has no return type
     * 
     * @param method
     *            The method to inspect
     * 
     * @return true if return type is void
     **/
    public static boolean hasMethodVoidReturnType(Method method) {
        return method.getReturnType().equals(Void.TYPE);
    }

    /**
     * Verify that the supplied method's signature matches return type and
     * parameters types
     * 
     * @param method
     *            Method to inspect
     * @param returnType
     *            Return type to match
     * @param parameters
     *            Parameter types to match
     * 
     * @throws IllegalArgumentException
     *             if method fails to match return type or parameters types
     */
    
    public static void verifyMethodSignature(Method method, Class<?> returnType, Class<?>... parameters) {
        if(method == null) {
                throw new IllegalArgumentException("Method is null");
        }
        if (!method.getReturnType().equals(returnType)) {
                throw new IllegalArgumentException("Method " + method + " return type " + method.getReturnType()
                        + " does not match: " + returnType);
        }
        Class<?>[] actualParameters = method.getParameterTypes();
        if (actualParameters.length != parameters.length) {
            throw new IllegalArgumentException("Method " + method + " number of parameters " + actualParameters.length
                    + " does not match: " + parameters.length);
        }
        if (!Arrays.equals(actualParameters,parameters)) {
            throw new IllegalArgumentException("Method " + method + " types of parameters "
                    + Arrays.toString(actualParameters) + " does not match: " + Arrays.toString(parameters));
        }
    }
}
