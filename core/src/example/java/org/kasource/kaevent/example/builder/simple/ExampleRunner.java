package org.kasource.kaevent.example.builder.simple;

import org.kasource.kaevent.config.KaEventConfigBuilder;
import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.event.dispatch.DefaultEventDispatcher;
import org.kasource.kaevent.example.builder.simple.event.TemperatureChangeEvent;
import org.kasource.kaevent.example.builder.simple.event.TemperatureChangeListener;


/**
 * Simple example that demonstrates listening to source objects.
 * 
 * @author wigforss
 **/
//CHECKSTYLE:OFF
///CLOVER:OFF
public class ExampleRunner {
	public static void main(String[] args) {
		Thermometer thermometer = new Thermometer();
		KaEventConfigBuilder config = new KaEventConfigBuilder();
		config.addEventBoundToInterface(TemperatureChangeEvent.class, TemperatureChangeListener.class);
		EventDispatcher eventDispatcher = 
		    new DefaultEventDispatcher(config.build());	
		
		Cooler cooler = new Cooler();
		Heater heater = new Heater();
		thermometer.setEventDispatcher(eventDispatcher);
		thermometer.setCooler(cooler);
		thermometer.setHeater(heater);
		thermometer.registerListers();
		new Thread(thermometer).start();
	}
}
