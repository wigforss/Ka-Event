package org.kasource.kaevent.example.spring.simple;

import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.channel.ChannelRegister;

import org.kasource.kaevent.event.config.EventConfig;

import org.kasource.kaevent.event.register.EventRegister;
import org.kasource.kaevent.example.spring.simple.event.TemperatureChangeEvent;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Simple example that demonstrates listening to source objects.
 * 
 * @author wigforss
 **/
///CLOVER:OFF
public class ExampleRunner {
	public static void main(String[] args) {
	    ApplicationContext context = new ClassPathXmlApplicationContext("org/kasource/kaevent/example/spring/simple/simple-context.xml");
	    Thermometer thermometer = (Thermometer) context.getBean("thermometer");
	    thermometer.run();
	  
	    
	    
	}
}
