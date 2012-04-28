package org.kasource.commons.util.reflection;

import java.lang.reflect.Type;
import java.util.EventObject;

/**
 * Class loading Utilities.
 * 
 * @author rikardwi
 **/
public class ClassUtils {
    
    
    
    /**
     * Loads and returns the class named className of type superClass.
     * 
     * @param <T> Type of the class
     * @param className Name of the class to load
     * @param superClass Type of the class to load
     * @return The loaded class of type superClass.
     * 
     * @throws IllegalArgumentException if the class with className could not be loaded or
     * if the that class does not extend the class supplied in the superClass parameter.
     **/
    @SuppressWarnings("unchecked")
    public static <T> Class<? extends T>  getClass(String className, Class<T> superClass) {
        try {
            Class<?> clazz = Class.forName(className);
         
            if(superClass == null || ! superClass.isAssignableFrom(clazz)) {
                throw new IllegalArgumentException("Class "+className+" must extend "+superClass+"!");
            }
            return (Class<? extends T>) clazz;
        }catch (ClassNotFoundException cnfe) {
            throw new IllegalArgumentException("Class "+className+" could not be found!",cnfe);
        }
    }

}
