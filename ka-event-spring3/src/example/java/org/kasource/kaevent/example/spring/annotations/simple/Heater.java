package org.kasource.kaevent.example.spring.annotations.simple;

import org.kasource.kaevent.annotations.listener.BeanListener;
import org.kasource.kaevent.annotations.listener.EventListenerFilter;
import org.kasource.kaevent.example.spring.annotations.simple.event.TemperatureChangeEvent;
import org.kasource.kaevent.example.spring.annotations.simple.event.TemperatureChangeListener;
import org.springframework.stereotype.Component;

///CLOVER:OFF
//CHECKSTYLE:OFF
@BeanListener("thermometer")
@EventListenerFilter("passFilter")
@Component
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
