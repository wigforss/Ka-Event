package org.kasource.commons.util.reflection;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Utility class for Interface based introspection of classes.
 * 
 * @author rikardwi
 **/
public class InterfaceUtils {
    
    private InterfaceUtils() {}
    
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
        List<Class<?>> interfaceList = new ArrayList<Class<?>>();
        Class<?> clazz = object.getClass();
        Class<?>[] interfaces = object.getClass().getInterfaces();
        interfaceList.addAll(Arrays.asList(interfaces));
        while((clazz = clazz.getSuperclass()) != null) {
            interfaces = clazz.getInterfaces();
            interfaceList.addAll(Arrays.asList(interfaces));
        }
          
        for(Class<?> i : interfaceList) {
            if(interfaceClass.isAssignableFrom(i)) {
                interfacesFound.add(i);
            }
        }
        return interfacesFound;
    }

    
    /**
     * Returns a set of all interfaces that the object implements that is annotated with annotation
     * 
     * @param object            Object to inspect
     * @param annotation        Annotation to match
     * 
     * @return All interfaces that the object implements that is annotated with annotation
     */
    public static Set<Class<?>> getAnnotatedInterfaces(Object object, Class<? extends Annotation> annotation) {
        Set<Class<?>> interfaceMatches = new HashSet<Class<?>>();
        Class<?>[] interfaces = object.getClass().getInterfaces();
        for(Class<?> i : interfaces) {
            if(i.getAnnotation(annotation) != null) {
                interfaceMatches.add(i);
            }
        }
        return interfaceMatches;
    }
    
}
