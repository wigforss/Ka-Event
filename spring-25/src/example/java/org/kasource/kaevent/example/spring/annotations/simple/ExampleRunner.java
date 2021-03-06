package org.kasource.kaevent.example.spring.annotations.simple;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Simple example that demonstrates listening to source objects.
 * 
 * @author wigforss
 **/
///CLOVER:OFF
//CHECKSTYLE:OFF
public class ExampleRunner {
	public static void main(String[] args) {
	    ApplicationContext context = 
	        new ClassPathXmlApplicationContext("org/kasource/kaevent/example/spring/annotations/simple/simple-context.xml");
	    Thermometer thermometer = (Thermometer) context.getBean("thermometer");
	    thermometer.run();
	  
	    
	    
	}
}
