package org.kasource.kaevent.annotations.listener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link java.util.EventListener} implementations annotated with this annotation will be
 * registered to listen to source objects by beanName of the value attribute, when a method 
 * annotated with @RegisterListener is called.
 * 
 * @author Rikard Wigforss
 * @version $Id: BeanListener.java 25 2010-09-05 13:06:39Z wigforss $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BeanListener {
    String[] value();
}
