package org.kasource.kaevent.example.cdi.simple;

import javax.enterprise.context.ApplicationScoped;

import org.kasource.kaevent.annotations.listener.BeanListener;
import org.kasource.kaevent.example.cdi.simple.event.OnTemperatureChange;
import org.kasource.kaevent.example.cdi.simple.event.TemperatureChangeEvent;


@BeanListener("thermometerSimple")
@ApplicationScoped
public class Cooler /*implements TemperatureChangeListener*/ {
	private boolean enabled = false;

	public boolean isEnabled() {
		return enabled;
	}

	@OnTemperatureChange
    public void temperatureChanged(TemperatureChangeEvent event) {
        if (event.getCurrentTemperature() > event.getSource().getOptimalTemperature()) {
            if (!enabled) {
                System.out.println("Cooler started.Message " + event.getId());
            }
            enabled = true;
        } else {
            if (enabled) {
                System.out.println("Cooler turned off. Message " + event.getId());
            }
            enabled = false;
        }

    }
}
