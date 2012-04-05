package org.kasource.commons.util.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Utility class for Annotation based introspection of classes.
 * 
 * @author rikardwi
 **/
public class AnnotationsUtils {
    
    private AnnotationsUtils() {}
    
    /**
     * Returns true if the class <i>clazz</i> or any of its super classes is annotated with an annotation
     * 
     * @param clazz
     *            The class to inspect
     * @param annotation
     *            An annotation to look for
     * 
     * @return
     */
    public static boolean isAnnotationPresent(Class<?> clazz, Class<? extends Annotation> annotation) {
        if (clazz.isAnnotationPresent(annotation)) {
            return true;
        }
        while((clazz = clazz.getSuperclass()) != null) {
            if (clazz.isAnnotationPresent(annotation)) {
                return true;
            }
        }
        return false;
    }
    
  
    
    /**
     * Returns the annotation of the annotationClass of the clazz or any of it super classes.
     * 
     * @param clazz
     *           The class to inspect.
     * @param annotationClass
     *           Class of the annotation to return
     *           
     * @return The annotation of annnotationClass if found else null.
     */
    public static <T extends Annotation> T getAnnotation(Class<?> clazz, Class<T> annotationClass) {
        
        T annotation = clazz.getAnnotation(annotationClass);
        if(annotation != null) {
            return annotation;
        }
        while((clazz = clazz.getSuperclass()) != null) {
            annotation = clazz.getAnnotation(annotationClass);
            if(annotation != null) {
                return annotation;
            }
        }
        return null;
    }
    

    /**
     * Returns true if the supplied class <i>clazz</i> has any declared method annotated
     * with <i>annotation</i>.
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
    
    /**
     * Returns the method of the supplied class <i>clazz</i> that is  annotated
     * with <i>annotation</i>.
     * 
     * @param clazz
     *            The class to inspect
     * @param annotation
     *            The annotation to look for
     * 
     * @return The method annotated with annotation 
     */
    public static Method getAnnotatatedMethod(Class<?> clazz, Class<? extends Annotation> annotation) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            Annotation[] methodAnnotations = method.getAnnotations();
            for (Annotation anno : methodAnnotations) {
                if (anno.annotationType().equals(annotation)) {
                    return method;
                }
            }
        }
        return null;
    }
    
    /**
     * Returns a map of methods annotated with an annotation from the annotations parameter.
     * 
     * @param clazz         The class to inspect
     * @param annotations   Method annotations to find methods for
     * @return Methods that is annotated with the supplied annotation set.
     **/
    public static Map<Class<? extends Annotation>, Method> findAnnotatedMethods(Class<?> clazz, Set<Class<? extends Annotation>> annotations) {
        Map<Class<? extends Annotation>, Method> annotatedMethods = new HashMap<Class<? extends Annotation>, Method>();
        Method method = null;
        for (Class<? extends Annotation> annotation : annotations) {          
            if((method = getAnnotatatedMethod(clazz, annotation)) != null) {
                annotatedMethods.put(annotation, method);
            }
        }
        while((clazz = clazz.getSuperclass()) != null) {
            for (Class<? extends Annotation> annotation : annotations) {
                if((method = getAnnotatatedMethod(clazz, annotation)) != null && !annotatedMethods.containsKey(annotation)) {
                    annotatedMethods.put(annotation, method);
                }
            }
        }
        
        return annotatedMethods;
    }
}
