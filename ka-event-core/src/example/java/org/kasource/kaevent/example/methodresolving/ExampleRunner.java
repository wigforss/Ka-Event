package org.kasource.kaevent.example.methodresolving;

import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.event.dispatch.DefaultEventDispatcher;




/**
 * Configures and starts the example application
 * 
 * @author rikard
 **/
///CLOVER:OFF
public class ExampleRunner {
	public static void main(String[] args) {
		Thermometer thermometer = new Thermometer();
		EventDispatcher eventDispatcher = new DefaultEventDispatcher("org.kasource.kaevent.example.methodresolving");
		Cooler cooler = new Cooler();
		Heater heater = new Heater();
		thermometer.setEventDispatcher(eventDispatcher);
		thermometer.setCooler(cooler);
		thermometer.setHeater(heater);
		thermometer.registerListers();
		new Thread(thermometer).start();
	}
}
