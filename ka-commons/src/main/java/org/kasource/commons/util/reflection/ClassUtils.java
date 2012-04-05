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
     * Loads the class named className and ensures the loaded class extends the interface supplied 
     * as the superInterface parameter.
     * 
     * @param <T> Interface to cast to
     * @param className Name of the class to load.
     * @param superInterface The interface of the class must implement.
     * 
     * @return The loaded class.
     * 
     * @throws IllegalArgumentException if class could not be loaded or the class does not extend/implement the 
     * the superInterface.
     **/
    @SuppressWarnings("unchecked")
    public static <T> Class<? extends T>  getInterfaceClass(String className, Class<T> superInterface) {
        try {
            
            Class<?> interfaceClass = Class.forName(className);
            Type[] superInterfaces =  interfaceClass.getGenericInterfaces();
            boolean foundInterface = false;
            for(Type interfaceType : superInterfaces) {
                if(superInterface.isAssignableFrom( (Class<?>) interfaceType )) {
                    foundInterface = true;
                    break;
                }
            }
            if(! foundInterface) {
                throw new IllegalArgumentException("Class "+className+" must extend "+superInterface+ "!");
            }
            return (Class<? extends T>) interfaceClass;
        }catch (ClassNotFoundException cnfe) {
            throw new IllegalArgumentException("Class "+className+" could not be found!",cnfe);
        }
    }
    
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
         
            if(superClass == null || ! EventObject.class.isAssignableFrom(superClass)) {
                throw new IllegalArgumentException("Class "+className+" must extend "+superClass+"!");
            }
            return (Class<? extends T>) clazz;
        }catch (ClassNotFoundException cnfe) {
            throw new IllegalArgumentException("Class "+className+" could not be found!",cnfe);
        }
    }

}
