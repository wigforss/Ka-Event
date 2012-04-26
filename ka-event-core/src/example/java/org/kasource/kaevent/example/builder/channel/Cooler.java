package org.kasource.kaevent.example.builder.channel;

import org.kasource.kaevent.example.builder.channel.event.TemperatureChangedEvent;
import org.kasource.kaevent.example.builder.channel.event.TemperatureChangedListener;



//CHECKSTYLE:OFF
///CLOVER:OFF
public class Cooler implements TemperatureChangedListener {
private boolean enabled = false;
	
	public boolean isEnabled() {
		return enabled;
	}


	@Override
	public void temperatureChanged(TemperatureChangedEvent event) {
		if (event.getCurrentTemperature() > event.getSource().getOptimalTemperatur())
        {
            if (!enabled) {
            	System.out.println("Cooler " + event.getSource().getId() + " started.");
            }
            enabled = true;
        } else {
            if (enabled) {
            	System.out.println("Cooler " + event.getSource().getId() + " turned off.");
            }
            enabled = false;
        }
		
	}

}
