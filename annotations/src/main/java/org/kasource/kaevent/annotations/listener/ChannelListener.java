package org.kasource.kaevent.annotations.listener;

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
 * @version $Id: ChannelListener.java 25 2010-09-05 13:06:39Z wigforss $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ChannelListener {
    String[] value();
}
