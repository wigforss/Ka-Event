package org.kasource.kaevent.cdi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.context.Dependent;

/**
 * Use this annotation to qualify the string
 * which holds the value of which packages and
 * sub-packages to scan for @Event annotated classes.
 * 
 * This value can also be a comma separated list of package names.
 * 
 * @author rikardwi
 **/
@Dependent
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EventScanPackage {
   
}
