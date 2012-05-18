package org.kasource.kaevent.example.custom;

import org.kasource.kaevent.example.custom.event.TemperatureChangeEvent;
import org.kasource.kaevent.example.custom.event.TemperatureChangeListener;



//CHECKSTYLE:OFF
///CLOVER:OFF
public class Heater implements TemperatureChangeListener {

	private boolean enabled = false;
	
	public boolean isEnabled() {
		return enabled;
	}

	

    
    @Override
    public void highTemperature(TemperatureChangeEvent event) {
        if (enabled) {
            System.out.println("Heater turned off.");
        }
        enabled = false;
        
    }

    
    @Override
    public void lowTemperature(TemperatureChangeEvent event) {
        if (!enabled) {
            System.out.println("Heater started.");
        }
        enabled = true;
        
    }

    
    @Override
    public void mediumTemperature(TemperatureChangeEvent event) {
        if (enabled) {
            System.out.println("Heater turned off.");
        }
        enabled = false;
        
    }

}
