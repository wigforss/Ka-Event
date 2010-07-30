package org.kasource.kaevent.core.listener.implementations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
/**
 * Annotate with Event Listener implementations with this annotation
 * to apply filtering. value is a string array of bean names of the 
 * filters to use.
 **/
public @interface EventListenerFilter {
	String[] value();
}
