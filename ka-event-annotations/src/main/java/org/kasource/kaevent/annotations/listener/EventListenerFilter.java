package org.kasource.kaevent.annotations.listener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
/**
 * Annotate with Event Listener implementations with this annotation
 * to apply filtering. value is a string array of bean names of the 
 * filters to use.
 **/
public @interface EventListenerFilter {
	String[] value();
}
