package org.kasource.kaevent.example.spring.simple;

import org.kasource.kaevent.example.spring.simple.event.TemperatureChangeEvent;
import org.kasource.kaevent.example.spring.simple.event.TemperatureChangeEventListener;



///CLOVER:OFF
public class Heater implements TemperatureChangeEventListener{

	private boolean enabled = false;
	
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void temperatureChanged(TemperatureChangeEvent event) {
		if (event.getCurrentTemperature() < event.getSource().getOptimalTemperatur())
        {
            if(enabled == false) {
            	System.out.println("Heater started.");
            }
            enabled = true;
        }
        else
        {
            if(enabled == true) {
            	System.out.println("Heater turned off.");
            }
            enabled =false;
        }

	}

	

}
