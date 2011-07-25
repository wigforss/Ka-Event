package org.kasource.kaevent.annotations.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.EventListener;

/**
 * Annotate Event classes with this annotation to register it with the event
 * listener framework.
 * 
 * NOTE: Only clases dervived from {@link java.util.EventObject} should be annotated
 * with this annotation.
 * 
 * @author rikard
 * @version $Id: Event.java 13 2010-08-18 09:09:46Z wigforss $
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Event {
    Class<? extends EventListener> listener();
    String[] channels() default {};
    boolean createChannels() default false;
}