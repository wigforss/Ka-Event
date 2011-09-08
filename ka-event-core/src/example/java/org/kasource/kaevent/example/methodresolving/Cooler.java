package org.kasource.kaevent.example.methodresolving;

import org.kasource.kaevent.example.methodresolving.event.TemperatureChangeEvent;
import org.kasource.kaevent.example.methodresolving.event.TemperatureChangeListener;

//CHECKSTYLE:OFF
///CLOVER:OFF
public class Cooler implements TemperatureChangeListener {

	private boolean enabled = false;
	
	public boolean isEnabled() {
		return enabled;
	}
	
	@Override
	public void temperatureDown(TemperatureChangeEvent event) {
		if (event.getCurrentTemperature() < event.getSource().getOptimalTemperatur()) {
			if (enabled) {
				System.out.println("Cooler turned off.");
			}
			enabled = false;		
		}
	}

	@Override
	public void temperatureUp(TemperatureChangeEvent event) {
		if (event.getCurrentTemperature() > event.getSource().getOptimalTemperatur())
        {
            if (!enabled) {
            	System.out.println("Cooler started.");
            }
            enabled = true;
        }
        
	}
	
	

}
