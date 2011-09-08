package org.kasource.kaevent.example.guice.simple;

import org.kasource.kaevent.annotations.listener.BeanListener;
import org.kasource.kaevent.annotations.listener.RegisterListener;
import org.kasource.kaevent.example.guice.simple.event.TemperatureChangeEvent;
import org.kasource.kaevent.example.guice.simple.event.TemperatureChangeListener;

import com.google.inject.Singleton;

//CHECKSTYLE:OFF
///CLOVER:OFF
@Singleton
@BeanListener("thermometer")
public class Heater implements TemperatureChangeListener {

	private boolean enabled = false;
	
	
	public Heater() {
		initialize();
	}
	
	@RegisterListener
	void initialize() {
		
	}
	
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
