package org.kasource.kaevent.annotations.event.methodresolving;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * When @{@link AnnotationMethodResolver} is used an {@link EventListener} interface, 
 * this annotation is used to indicate which method that should be invoked by providing
 * a keyword in its value attribute. The attribute will the be matched by the result of calling
 * the @{@link org.kasource.kaevent.event.kenai.sadelf.annotations.event.EventKeyword} method on the event itself.
 * 
 * @author rikard
 * @version $Id: KeywordCase.java 7 2010-08-02 09:31:05Z wigforss $
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface KeywordCase {
    String value();
}
