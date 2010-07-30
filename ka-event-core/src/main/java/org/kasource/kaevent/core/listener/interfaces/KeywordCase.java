package org.kasource.kaevent.core.listener.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * When @{@link AnnotationMethodResolver} is used an {@link EventListener} interface, 
 * this annotation is used to indicate which method that should be invoked by providing
 * a keyword in its value attribute. The attribute will the be matched by the result of calling
 * the @{@link com.kenai.sadelf.annotations.event.EventKeyword} method on the event itself.
 * 
 * @author rikard
 * @version $Id$
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface KeywordCase {
    String value();
}
