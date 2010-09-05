package org.kasource.kaevent.listener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.kasource.kaevent.listener.implementations.BeanListener;
import org.kasource.kaevent.listener.implementations.ChannelListener;

/**
 * Annotate any method on a class annotated with @{@link ChannelListener} or @{@link BeanListener} to 
 * register as a listener when that method is executed. 
 * Use this annotation together with @PostConstruct to register as a listener or annotate another 
 * method such as a constructor or other initialization method.
 * 
 * 
 * @author Rikard Wigforss
 * @version $Id$
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR,ElementType.METHOD})
public @interface RegisterListener {

}
