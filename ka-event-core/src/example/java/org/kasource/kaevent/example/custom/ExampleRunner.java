package org.kasource.kaevent.example.custom;



import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.event.dispatch.DefaultEventDispatcher;

/**
 * Example which uses a custom method resolver found by invoking a factory method, see TemperatureChangeEventListener for details. 
 *  
 * @author wigforss
 **/
public class ExampleRunner {
	public static void main(String[] args) {
		Thermometer thermometer = new Thermometer();
		EventDispatcher eventDispatcher = new DefaultEventDispatcher(ExampleRunner.class.getPackage().getName());	
		Cooler cooler = new Cooler();
		Heater heater = new Heater();
		thermometer.setEventDispatcher(eventDispatcher);
		thermometer.setCooler(cooler);
		thermometer.setHeater(heater);
		thermometer.registerListers();
		new Thread(thermometer).start();
	}
}
