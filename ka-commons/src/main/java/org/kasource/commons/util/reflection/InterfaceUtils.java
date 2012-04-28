package org.kasource.commons.util.reflection;

import java.util.HashSet;
import java.util.Set;

import org.kasource.commons.reflection.filter.classes.ClassFilter;

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
     * * @param clazz
     *            Object to inspect
     * @param interfaceClass
     *            Interface to match   
     * 
     * @return true if object's class implements the <i>interfaceClass</i>.
     **/
    public static boolean implementsInterface(Class<?> clazz,Class<?> interfaceClass) {
        return interfaceClass.isAssignableFrom(clazz);
    }
    
    
    /**
     * Returns a set of interfaces that the from clazz that passes the supplied filter.
     * This method also inspects any interfaces implemented by super classes.
     * 
     * @param clazz             The class to inspect
     * @param filter            The class filter to use.
     * 
     * @return all Interface classes from clazz that passes the filter.
     */
    public static  Set<Class<?>> getInterfaces(Class<?> clazz, ClassFilter filter) {
       
        Set<Class<?>> interfacesFound = getDeclaredInterfaces(clazz, filter);
        
        while((clazz = clazz.getSuperclass()) != null) {
            interfacesFound.addAll(getDeclaredInterfaces(clazz, filter));
        }
        return interfacesFound;
       
    }

    
    /**
     * Returns a set of interfaces that the from clazz that passes the supplied filter.
     * 
     * @param clazz             The class to inspect
     * @param filter            The class filter to use.
     * 
     * @return all Interface classes from clazz that passes the filter.
     */
    public static Set<Class<?>> getDeclaredInterfaces(Class<?> clazz, ClassFilter filter) {
        Set<Class<?>> interfacesFound = new HashSet<Class<?>>();

        Class<?>[] interfaces = clazz.getInterfaces();
        for(Class<?> interfaceClass : interfaces) {
            if(filter.passFilter(interfaceClass)) {
                interfacesFound.add(interfaceClass);
            }
        }
        return interfacesFound;
    }
    
    
    
}
