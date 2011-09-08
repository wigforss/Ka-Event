package org.kasource.kaevent.example.spring.simple;

import org.kasource.kaevent.example.spring.simple.event.TemperatureChangeEvent;
import org.kasource.kaevent.example.spring.simple.event.TemperatureChangeListener;

///CLOVER:OFF
//CHECKSTYLE:OFF
public class Heater implements TemperatureChangeListener {

	private boolean enabled = false;
	
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void temperatureChanged(TemperatureChangeEvent event) {
		if (event.getCurrentTemperature() < event.getSource().getOptimalTemperatur()) {
            if (!enabled) {
            	System.out.println("Heater started.");
            }
            enabled = true;
        } else {
            if (enabled) {
            	System.out.println("Heater turned off.");
            }
            enabled = false;
        }

	}

	

}
