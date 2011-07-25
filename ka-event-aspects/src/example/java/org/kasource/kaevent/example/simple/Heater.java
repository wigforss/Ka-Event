package org.kasource.kaevent.example.simple;

import org.kasource.kaevent.annotations.listener.BeanListener;
import org.kasource.kaevent.annotations.listener.RegisterListener;
import org.kasource.kaevent.example.simple.event.TemperatureChangeEvent;
import org.kasource.kaevent.example.simple.event.TemperatureChangeEventListener;

///CLOVER:OFF
@BeanListener("thermometer")
public class Heater implements TemperatureChangeEventListener{
    private boolean enabled = false;
    
        public Heater() {
            initialize();
        }
    
        @RegisterListener
        private void initialize() {
            
        }
        
        
	
	
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
