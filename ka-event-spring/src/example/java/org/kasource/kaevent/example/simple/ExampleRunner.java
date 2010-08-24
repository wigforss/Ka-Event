package org.kasource.kaevent.example.simple;

import org.kasource.kaevent.channel.Channel;
import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.event.config.EventConfig;
import org.kasource.kaevent.event.dispatch.DefaultEventDispatcher;
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
	    ApplicationContext context = new ClassPathXmlApplicationContext("org/kasource/kaevent/example/simple/simple-context.xml"); 
	    Channel channel = (Channel) context.getBean("testChannel");
	    System.out.println(channel.getName());
	    EventConfig event = (EventConfig) context.getBean("tempratureChangeEvent");
	    System.out.println(event.getEventClass());
	/*	Thermometer thermometer = new Thermometer();
		EventDispatcher eventDispatcher = new DefaultEventDispatcher(ExampleRunner.class.getPackage().getName().replace('.', '/')+"/simple-config.xml");	
		Cooler cooler = new Cooler();
		Heater heater = new Heater();
		thermometer.setEventDispatcher(eventDispatcher);
		thermometer.setCooler(cooler);
		thermometer.setHeater(heater);
		thermometer.registerListers();
		new Thread(thermometer).start(); */
	    
	    System.out.println("Done");
	}
}
