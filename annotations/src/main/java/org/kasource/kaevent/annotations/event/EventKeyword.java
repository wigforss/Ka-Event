package org.kasource.kaevent.annotations.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate one method on a class with the @Event annotation with this
 * annotation to indicate which method returns the keyword value used by the
 * method resolver when using the strategy MethodResolverStrategy.EVENT_KEYWORD
 * 
 * @author rikard
 * @version $Id$
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventKeyword {

}
