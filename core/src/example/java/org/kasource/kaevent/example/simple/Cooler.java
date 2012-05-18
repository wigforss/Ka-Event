package org.kasource.kaevent.example.simple;

import org.kasource.kaevent.example.simple.event.TemperatureChangeEvent;
import org.kasource.kaevent.example.simple.event.TemperatureChangeListener;

//CHECKSTYLE:OFF
///CLOVER:OFF
public class Cooler implements TemperatureChangeListener {

	
	private boolean enabled = false;
	
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void temperatureChanged(TemperatureChangeEvent event) {
		if (event.getCurrentTemperature() > event.getSource().getOptimalTemperatur())
        {
            if (!enabled) {
            	System.out.println("Cooler started.");
            }
            enabled = true;
        } else {
            if (enabled) {
            	System.out.println("Cooler turned off.");
            }
            enabled = false;
        }

	}

}
