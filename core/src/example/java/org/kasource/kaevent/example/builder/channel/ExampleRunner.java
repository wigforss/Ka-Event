package org.kasource.kaevent.example.builder.channel;


import org.kasource.kaevent.config.KaEventConfigBuilder;
import org.kasource.kaevent.event.EventDispatcher;
import org.kasource.kaevent.event.dispatch.DefaultEventDispatcher;
import org.kasource.kaevent.example.builder.channel.event.TemperatureChangedEvent;
import org.kasource.kaevent.example.builder.channel.event.TemperatureChangedListener;

/**
 * Example that demonstrate usage of Channels. 
 * 
 * The CommandConsole class listens to the temperatureChannel instead of
 * a thermometer object.
 * 
 * @author wigforss
 **/
//CHECKSTYLE:OFF
///CLOVER:OFF
public class ExampleRunner {
      
	public static void main(String[] args) {
		EventDispatcher eventDispatcher = getEventDispatcher();
		final int numThermometers = 10;
		for (int i = 0; i < numThermometers; ++i) {
			createThermometer(eventDispatcher, i);
		}
	}
		
	@SuppressWarnings("unchecked")
    private static EventDispatcher getEventDispatcher() {
	    KaEventConfigBuilder config = new KaEventConfigBuilder();
	    config.addEventBoundToInterface(TemperatureChangedEvent.class, TemperatureChangedListener.class)
	          .addChannel("temperatureChannel",TemperatureChangedEvent.class);
		EventDispatcher eventDispatcher = 
		    new DefaultEventDispatcher(config.build());
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
		thermometer.setOptimalTemperatur(thermometer.getOptimalTemperatur() + tempDiff);
		new Thread(thermometer).start();
	}
}
