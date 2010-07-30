package org.kasource.kaevent.core.listener.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method is resolved by an object that implements the {@link com.MethodResolving.sadelf.event.methodresolver.MethodResolver}
 * interface, which can be found by using a factory. The attributes factoryClass
 * and factoryMethods have to be set to locate the factory, also the optional
 * factoryMethodArgument can be used to supply one string argument to the
 * factory method call.
 * 
 * @author rikard
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FactoryMethodResolver {
	  Class<?> factoryClass();

	  String factoryMethod();

	  String factoryMethodArgument() default "";
}
