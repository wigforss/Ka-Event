package org.kasource.kaevent.example.simple;

import org.kasource.kaevent.example.simple.event.TemperatureChangeEvent;
import org.kasource.kaevent.example.simple.event.TemperatureChangeEventListener;


public class Cooler implements TemperatureChangeEventListener{

	
	private boolean enabled = false;
	
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void temperatureChanged(TemperatureChangeEvent event) {
		if (event.getCurrentTemperature() > event.getSource().getOptimalTemperatur())
        {
            if(enabled == false) {
            	System.out.println("Cooler started.");
            }
            enabled = true;
        }
        else
        {
            if(enabled == true) {
            	System.out.println("Cooler turned off.");
            }
            enabled =false;
        }

	}

}
