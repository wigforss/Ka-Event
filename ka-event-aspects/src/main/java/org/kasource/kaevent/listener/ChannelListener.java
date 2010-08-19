package org.kasource.kaevent.listener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link java.util.EventListener} implementations annotated with this annotation will be
 * registered at the named channels when a method 
 * annotated with @RegisterListener is called
 * 
 * @author Rikard Wigforss
 * @version $Id$
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ChannelListener {
    String[] value();
}
