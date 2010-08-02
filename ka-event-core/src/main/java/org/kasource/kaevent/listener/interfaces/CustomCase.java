package org.kasource.kaevent.listener.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Marker annotation for custom case annotations.
 * Any annotation annotated with this will be considered a @{@link KeywordCase}. 
 * That annotation needs to define the value attribute for the "case 
 * lookup" to work, if value can't be accessed an exception will be thrown.
 * 
 * NOTE that any annotation annotated with CustomCase needs be accessible in runtime thus
 * use @{@link Retention}(RetentionPolicy.RUNTIME)
 * 
 * @author rikard
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface CustomCase {

}
