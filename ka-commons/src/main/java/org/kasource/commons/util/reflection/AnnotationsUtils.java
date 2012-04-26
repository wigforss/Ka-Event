package org.kasource.commons.util.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.kasource.commons.reflection.filter.methods.MethodFilterBuilder;

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
        Set<Method> methods = MethodUtils.getDeclaredMethods(clazz, new MethodFilterBuilder().annotated(annotation).build());
        return !methods.isEmpty();
    }
    
    /**
     * Returns the first method found of the supplied class <i>clazz</i> that is  annotated
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
        Set<Method> methods = MethodUtils.getDeclaredMethods(clazz, new MethodFilterBuilder().annotated(annotation).build());
        if(methods.isEmpty()) {
            return null;
        }
        return methods.iterator().next();
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
        
        for (Class<? extends Annotation> annotation : annotations) { 
            Set<Method> methods = MethodUtils.getMethods(clazz, new MethodFilterBuilder().annotated(annotation).build());
            for(Method method : methods) {
                annotatedMethods.put(annotation, method);
            }
        }
        return annotatedMethods;
    }
}
