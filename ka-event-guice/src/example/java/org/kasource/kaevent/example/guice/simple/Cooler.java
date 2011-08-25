package org.kasource.kaevent.example.guice.simple;

import org.kasource.kaevent.annotations.listener.BeanListener;
import org.kasource.kaevent.annotations.listener.RegisterListener;
import org.kasource.kaevent.example.guice.simple.event.TemperatureChangeEvent;
import org.kasource.kaevent.example.guice.simple.event.TemperatureChangeEventListener;

import com.google.inject.Singleton;


//CHECKSTYLE:OFF
///CLOVER:OFF
@Singleton
@BeanListener("thermometer")
public class Cooler implements TemperatureChangeEventListener {

	
	private boolean enabled = false;
	
	
	public Cooler() {
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
		if (event.getCurrentTemperature() > event.getSource().getOptimalTemperatur()) {
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
