package org.kasource.kaevent.annotations.event.methodresolving;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method is resolved by a bean that implements the {@link org.kasource.kaevent.listener.interfaces.MethodResolving.sadelf.event.methodresolver.MethodResolver}
 * interface the bean name have to be supplied as the value attribute.
 * 
 * @author rikard
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BeanMethodResolver {
	 String value();
}
