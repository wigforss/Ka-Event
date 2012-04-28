package org.kasource.commons.util.reflection;

import java.lang.reflect.Constructor;

/**
 * Utility class for creating object instances using Reflection Based Construction.
 * 
 * @author rikardwi
 **/
public class ConstructorUtils {
    
    /**
     * Creates and returns a new instance of class with name className, loading the class and using the default constructor.
     * 
     * @param <T>
     * @param className
     * @param ofType
     * @return a new instance of class loaded from className.
     * 
     * @throws IllegalStateException if className could not be loaded or if that class does not have a default constructor
     * or if the loaded class is not of the supplied type (ofType).
     */
    public static <T> T getInstance(String className, Class<T> ofType) throws IllegalStateException {
        return getInstance(className, ofType, new Class<?>[]{}, new Object[]{});
    }
    
    /**
     * Returns a new object of <i>className</i>. The objected is casted to the <i>ofType</i>, which is either super class or interface of the className class.
     * 
     * @param className         Name of the class to instanciate an object of
     * @param ofType            An super class or interface of the className class.
     * @param constructorArgs   Constructor arguments to use when creating a new instance
     * 
     * @return A new instance of class with name className casted to the ofType class.
     *
     * @throws IllegalStateException if className could not be loaded or if that class does not have a matching constructor
     * to the constructorParam or if the loaded class is not of the supplied type (ofType).
     **/
    @SuppressWarnings("unchecked")
    public static <T> T getInstance(String className, Class<T> ofType, Class<?>[] constructorParams, Object[] constructorArgs) {
        Constructor<?> constructor = null;
        try {
            Class<?> clazz = Class.forName(className);
            constructor = clazz.getConstructor(constructorParams);
            return (T) constructor.newInstance(constructorArgs);
        } catch (Exception e) {       
                String errorMessage = "Could not instanceiate " + className+".";
                if(constructor == null) {
                    errorMessage += " No constructor found with parameters "+constructorParams;
                } else if(constructorParams.length == 0) {
                    errorMessage += " Using default constructor.";
                } else {
                    errorMessage += " " + constructor;
                }
                throw new IllegalStateException(errorMessage, e);          
        } 
    }
    
}
