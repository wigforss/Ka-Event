package org.kasource.kaevent.example.spring.configure.simple;

import org.kasource.kaevent.config.KaEventConfigurationClass;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


/**
 * Simple example that demonstrates listening to source objects.
 * 
 * @author wigforss
 **/
///CLOVER:OFF
//CHECKSTYLE:OFF
public class ExampleRunner {
	public static void main(String[] args) {
	    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
	    KaEventConfigurationClass.scan(ExampleRunner.class.getPackage().getName());
	    context.register(KaEventConfigurationClass.class);
	    context.scan(ExampleRunner.class.getPackage().getName());
	    context.refresh();
	    Thermometer thermometer = (Thermometer) context.getBean("thermometer");
	    thermometer.run();
	  
	    
	    
	}
}
