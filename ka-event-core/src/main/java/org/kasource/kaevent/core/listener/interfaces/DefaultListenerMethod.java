package org.kasource.kaevent.core.listener.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * If more than one method exists in a class annotated with @{@link AnnotationMethodResolver} this
 * annotation is used to resolve which to trigger as default.
 * 
 * @author rikard
 * @version $Id$
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DefaultListenerMethod {
   
}
