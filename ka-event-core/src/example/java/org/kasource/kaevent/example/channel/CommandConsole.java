package org.kasource.kaevent.example.channel;

import org.kasource.kaevent.example.channel.event.TemperatureChangedEvent;
import org.kasource.kaevent.example.channel.event.TemperatureChangedListener;

//CHECKSTYLE:OFF
///CLOVER:OFF
public class CommandConsole implements TemperatureChangedListener {

	@Override
	public void temperatureChanged(TemperatureChangedEvent event) {
		if (event.getCurrentTemperature() > event.getSource().getOptimalTemperatur()) {
			System.out.println("Warning " + event.getSource() + " overheating!");
		}
	}	
}
