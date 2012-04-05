package org.kasource.kaevent.example.guice.channel;

import org.kasource.kaevent.annotations.listener.BeanListener;
import org.kasource.kaevent.annotations.listener.RegisterListener;
import org.kasource.kaevent.example.guice.channel.event.OnTemperatureChanged;
import org.kasource.kaevent.example.guice.channel.event.TemperatureChangedEvent;

import com.google.inject.Singleton;

//CHECKSTYLE:OFF
///CLOVER:OFF
@Singleton
@BeanListener("thermometer")
public class Heater {
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

    @OnTemperatureChanged
    public void temperatureChanged(TemperatureChangedEvent event) {
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
