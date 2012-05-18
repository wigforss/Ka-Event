package org.kasource.kaevent.annotations.listener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotate any method on a class annotated with @{@link ChannelListener} or @{@link BeanListener} to 
 * unregister as a listener when that method is executed. 
 * Use this annotation together with @PreDestroy to unregister as a listener or annotate another 
 * method which is called at resource clean-up.
 * 
 * Suitable candidate methods to annotate with @Unregister could be close(), {@link Object#finalize() finalize()} or destroy() 
 * 
 * @author Rikard Wigforss
 * @version $Id: UnregisterListener.java 25 2010-09-05 13:06:39Z wigforss $
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UnregisterListener {

}
