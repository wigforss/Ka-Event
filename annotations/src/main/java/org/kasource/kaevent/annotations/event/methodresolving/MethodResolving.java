/**
 * 
 */
package org.kasource.kaevent.annotations.event.methodresolving;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @author rikardwigforss
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodResolving {
    MethodResolverType value();
}
