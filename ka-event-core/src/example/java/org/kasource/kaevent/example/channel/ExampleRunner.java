package org.kasource.kaevent.example.channel;


import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.event.dispatch.DefaultEventDispatcher;

/**
 * Example that demonstrate usage of Channels. 
 * 
 * The CommandConsole class listens to the temperatureChannel instead of
 * a thermometer object.
 * 
 * @author wigforss
 **/
///CLOVER:OFF
public class ExampleRunner {
	public static void main(String[] args) {
		EventDispatcher eventDispatcher = getEventDispatcher();
		for(int i=0; i < 10; ++i) {
			createThermometer(eventDispatcher,i);
		}
	}
		
	private static EventDispatcher getEventDispatcher() {
		EventDispatcher eventDispatcher = new DefaultEventDispatcher(ExampleRunner.class.getPackage().getName().replace('.', '/')+"/channel-config.xml");
		eventDispatcher.registerListenerAtChannel(new CommandConsole(), "temperatureChannel");	
		return eventDispatcher;
	}
	
	private static void createThermometer(EventDispatcher eventDispatcher, int tempDiff) {
		Thermometer thermometer = new Thermometer();
		Cooler cooler = new Cooler();
		Heater heater = new Heater();
		thermometer.setEventDispatcher(eventDispatcher);
		thermometer.setCooler(cooler);
		thermometer.setHeater(heater);
		thermometer.registerListers();
		thermometer.setOptimalTemperatur(thermometer.getOptimalTemperatur()+tempDiff);
		new Thread(thermometer).start();
	}
}
