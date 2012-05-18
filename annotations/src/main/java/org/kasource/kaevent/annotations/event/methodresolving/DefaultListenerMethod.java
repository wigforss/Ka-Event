package org.kasource.kaevent.annotations.event.methodresolving;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * If more than one method exists in a class annotated with @{@link AnnotationMethodResolver} this
 * annotation is used to resolve which to trigger as default.
 * 
 * @author rikard
 * @version $Id: DefaultListenerMethod.java 7 2010-08-02 09:31:05Z wigforss $
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DefaultListenerMethod {
   
}
