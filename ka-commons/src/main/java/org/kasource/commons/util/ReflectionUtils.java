package org.kasource.commons.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Reflection utility methods used.
 * 
 * @author rikard
 * @version $Id$
 **/
public class ReflectionUtils {

    // //////////////////////////////////////
    //
    // Annotations
    //  
    // /////////////////////////////////////

    /**
     * Returns true if the class <i>clazz</i> is annotated with any annotation
     * 
     * @param clazz
     *            The class to inspect
     * @param annotation
     *            An annotation to look for
     * 
     * @return
     */
    public static boolean hasAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
        if (clazz.getAnnotation(annotation) != null) {
            return true;
        }
        return false;
    }

    /**
     * Returns true if the supplied class <i>clazz</i> has any declared method annotated
     * with any annotation in <i>annotations</i>.
     * 
     * @param clazz
     *            The class to inspect
     * @param annotation
     *            The annotation to look for
     * 
     * @return true if clazz has any method annotated with any annotation 
     */
    public static boolean hasAnnotatatedMethod(Class<?> clazz, Class<? extends Annotation> annotation) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            Annotation[] methodAnnotations = method.getAnnotations();
            for (Annotation anno : methodAnnotations) {
                if (anno.annotationType().equals(annotation)) {
                    return true;
                }
            }
        }
        return false;
    }

    // //////////////////////////////////////
    //
    // Interfaces
    //  
    // /////////////////////////////////////

    /**
     * Returns true if object's class implements the <i>interfaceClass</i>.
     * 
     * * @param object
     *            Object to inspect
     * @param interfaceClass
     *            Interface to match   
     * 
     * @return true if object's class implements the <i>interfaceClass</i>.
     **/
    public static boolean implementsInterface( Object object,Class<?> interfaceClass) {
        return interfaceClass.isAssignableFrom(object.getClass());
    }
    
    
    /**
     * Returns a set of interfaces that the object implements which extends the <i>interfaceClass</i>
     * supplied as argument.
     * 
     * @param object            The object to inspect
     * @param interfaceClass    The interface class to compare with
     * 
     * @return all Interface classes the object implements that extends interfaceClass
     */
    public static  Set<Class<?>> getInterfacesExtending(Object object, Class<?> interfaceClass) {
        Set<Class<?>> interfacesFound = new HashSet<Class<?>>();
        Class<?>[] interfaces = object.getClass().getInterfaces();
        for(Class<?> i : interfaces) {
            if(interfaceClass.isAssignableFrom(i)) {
                interfacesFound.add(i);
            }
        }
        return interfacesFound;
    }

    // //////////////////////////////////////
    //
    // Methods
    //  
    // /////////////////////////////////////

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
     * parameters in its method signature
     * 
     * @param clazz
     *            The class to inspect
     * @param params
     *            The method parameters to match
     * 
     * @return methods that match the params argument in their method signature
     * 
     **/
    public static Set<Method> getDeclaredMethods(Class<?> clazz, Class<?>... params) {
        Method[] methods = clazz.getDeclaredMethods();
        Set<Method> matches = new HashSet<Method>();
        for (Method method : methods) {
            
            if (Arrays.equals(method.getParameterTypes(), params)) {
                matches.add(method);
            }
        }
        return matches;
    }

    /**
     * Returns the methods declared by clazz which matches the supplied
     * parameters and return type in its method signature
     * 
     * @param clazz
     *            The class to inspect
     * @param params
     *            The method parameters to match
     * 
     * @return methods that match the params argument in their method signature
     * 
     **/
    public static Set<Method> getDeclaredMethodsMatchingReturnType(Class<?> clazz, Class<?> returnType, Class<?>... params) {
        Method[] methods = clazz.getDeclaredMethods();
        Set<Method> matches = new HashSet<Method>();
        for (Method method : methods) {
            if (Arrays.equals(method.getParameterTypes(), params) && method.getReturnType().equals(returnType)) {
                matches.add(method);
            }
        }
        return matches;
    }

    /**
     * Return the method on <i>clazz</i> that is annotated with the annotation.
     * If more than one method is found the first found will be returned
     * 
     * @param clazz
     *            Class to use when finding the method
     * 
     * @param annotation
     *            The annotation to match
     * 
     * @return The first method found which is annotated with annotation.
     */
    public static Method getDeclaredAnnotatedMethod(Class<?> clazz, Class<? extends Annotation> annotation) {
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            if (method.getAnnotation(annotation) != null) {
                return method;
            }
        }
        return null;
    }

    /**
     * Return the declared methods on <i>clazz</i> that are annotated with the annotation.
     * 
     * @param clazz
     *            Class to use when finding the method
     * 
     * @param annotation
     *            The annotation to match
     * 
     * @return The methods found which is annotated with annotation.
     */
    public static Set<Method> getDeclaredAnnotatedMethods(Class<?> clazz, Class<? extends Annotation> annotation) {
        Method[] methods = clazz.getDeclaredMethods();
        Set<Method> methodSet = new HashSet<Method>();
        methodSet.addAll(Arrays.asList(methods));
        return filterAnnotatedMethods(methodSet, annotation);
    }
    
    /**
     * Return the methods on <i>clazz</i> that are annotated with the annotation.
     * 
     * @param clazz
     *            Class to use when finding the method
     * 
     * @param annotation
     *            The annotation to match
     * 
     * @return The methods found which is annotated with annotation.
     */
    public static Set<Method> getAnnotatedMethods(Class<?> clazz, Class<? extends Annotation> annotation) {
        Method[] methods = clazz.getMethods();
        Set<Method> methodSet = new HashSet<Method>();
        methodSet.addAll(Arrays.asList(methods));
        return filterAnnotatedMethods(methodSet, annotation);
    }
    
    /**
     * Filter the methods
     * @param methods
     * @param annotation
     * @return
     */
    public static Set<Method> filterAnnotatedMethods(Set<Method> methods, Class<? extends Annotation> annotation) {
        Set<Method> matches = new HashSet<Method>();
        for (Method method : methods) {
            if (method.getAnnotation(annotation) != null) {
                matches.add(method);
            }
        }
        return matches;
    }

    /**
     * Return the declared methods on <i>clazz</i> that is annotated with an annotation
     * that is annotated with the inheritAnnotation.
     * 
     * 
     * @param clazz
     *            Class to use when finding the method
     * 
     * @param inheritedAnnotation
     *           "Parent" annotation to match
     * 
     * @return The methods found which is annotated with annotation.
     */
    public static Set<Method> getDeclaredInheritlyAnnotatedMethods(Class<?> clazz, Class<? extends Annotation> inheritedAnnotation) {
        Method[] methods = clazz.getDeclaredMethods();
        Set<Method> methodSet = new HashSet<Method>();
        methodSet.addAll(Arrays.asList(methods));
        return filterInheritlyAnnotatedMethods(methodSet, inheritedAnnotation);
    }
    
    /**
     * Return the methods on <i>clazz</i> that is annotated with an annotation
     * that is annotated with the inheritAnnotation.
     * 
     * 
     * @param clazz
     *            Class to use when finding the method
     * 
     * @param inheritedAnnotation
     *            The annotation to match
     * 
     * @return The methods found which is annotated with annotation.
     */
    public static Set<Method> getInheritlyAnnotatedMethods(Class<?> clazz, Class<? extends Annotation> inheritedAnnotation) {
        Method[] methods = clazz.getMethods();
        Set<Method> methodSet = new HashSet<Method>();
        methodSet.addAll(Arrays.asList(methods));
        return filterInheritlyAnnotatedMethods(methodSet, inheritedAnnotation);
    }
    
    /**
     * Filters and returns methods that is annotated with an annotation
     * that is annotated with the inheritAnnotation.
     * 
     * @param methods                   Methods to inspect
     * @param inheritedAnnotation       "Parent" annotation to match
     * 
     * @return Set of all methods that had a parent annotation that of <i>inheritedAnnotation</i>.
     **/
    public static Set<Method> filterInheritlyAnnotatedMethods(Set<Method> methods, Class<? extends Annotation> inheritedAnnotation) {
        Set<Method> matches = new HashSet<Method>();
        for (Method method : methods) {
            Annotation[] annotations = method.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().getAnnotation(inheritedAnnotation) != null) {
                    matches.add(method);
                }
            }
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
