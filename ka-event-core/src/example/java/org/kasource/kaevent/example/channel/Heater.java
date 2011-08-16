package org.kasource.kaevent.example.channel;

import org.kasource.kaevent.example.channel.event.TemperatureChangedEvent;
import org.kasource.kaevent.example.channel.event.TemperatureChangedEventListener;

//CHECKSTYLE:OFF
///CLOVER:OFF
public class Heater implements TemperatureChangedEventListener {
private boolean enabled = false;
	
	public boolean isEnabled() {
		return enabled;
	}

	

	@Override
	public void temperatureChanged(TemperatureChangedEvent event) {
		if (event.getCurrentTemperature() < event.getSource().getOptimalTemperatur())
        {
            if (!enabled) {
            	System.out.println("Heater " + event.getSource().getId() + " started.");
            }
            enabled = true;
        } else {
            if (enabled) {
            	System.out.println("Heater " + event.getSource().getId() + " turned off.");
            }
            enabled = false;
        }
		
	}
}
