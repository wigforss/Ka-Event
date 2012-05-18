package org.kasource.kaevent.annotations.listener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;




/**
 * Annotate any method on a class annotated with @{@link ChannelListener} or @{@link BeanListener} to 
 * register as a listener when that method is executed. 
 * Use this annotation together with @PostConstruct to register as a listener or annotate another 
 * method such as a constructor or other initialization method.
 * 
 * 
 * @author Rikard Wigforss
 * @version $Id: RegisterListener.java 25 2010-09-05 13:06:39Z wigforss $
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR,ElementType.METHOD})
public @interface RegisterListener {

}
