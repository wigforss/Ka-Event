package org.kasource.kaevent.example.cdi.simple;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;

import org.kasource.kaevent.annotations.listener.BeanListener;

import org.kasource.kaevent.example.cdi.simple.event.TemperatureChangeEvent;
import org.kasource.kaevent.example.cdi.simple.event.TemperatureChangeListener;


@BeanListener("thermometerSimple")
@ApplicationScoped 
public class Heater implements TemperatureChangeListener {
	private boolean enabled = false;

	public boolean isEnabled() {
		return enabled;
	}

	@Override
    public void temperatureChanged(TemperatureChangeEvent event) {
        if (event.getCurrentTemperature() < event.getSource().getOptimalTemperature()) {
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
