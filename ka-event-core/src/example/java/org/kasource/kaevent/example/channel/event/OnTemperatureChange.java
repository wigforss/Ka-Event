package org.kasource.kaevent.example.channel.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Can be used as an alternative to the interface 
 * TemperatureChangeListener. See commented line in 
 * TemperatureChangeEvent.
 * 
 * @author rikardwi
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnTemperatureChange {

}
