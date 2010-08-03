package org.kasource.kaevent.example.methodresolving;

import org.kasource.kaevent.example.methodresolving.event.TemperatureChangeEvent;
import org.kasource.kaevent.example.methodresolving.event.TemperatureChangeEventListener;


public class Heater implements TemperatureChangeEventListener{

	private boolean enabled = false;
	
	public boolean isEnabled() {
		return enabled;
	}
	
	@Override
	public void temperatureDown(TemperatureChangeEvent event) {
		if (event.getCurrentTemperature() < event.getSource().getOptimalTemperatur())
        {
            if(enabled == false) {
            	System.out.println("Heater started.");
            }
            enabled = true;
        }
        
	}

	@Override
	public void temperatureUp(TemperatureChangeEvent event) {
		if (event.getCurrentTemperature() > event.getSource().getOptimalTemperatur()) {
		 if(enabled == true) {
         	System.out.println("Heater turned off.");
         }
         enabled =false;
		}
	}

}
