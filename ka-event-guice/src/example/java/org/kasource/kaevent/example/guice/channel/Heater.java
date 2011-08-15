package org.kasource.kaevent.example.guice.channel;

import org.kasource.kaevent.annotations.listener.BeanListener;
import org.kasource.kaevent.annotations.listener.RegisterListener;
import org.kasource.kaevent.example.guice.channel.event.TemperatureChangedEvent;
import org.kasource.kaevent.example.guice.channel.event.TemperatureChangedEventListener;

import com.google.inject.Singleton;



///CLOVER:OFF
@Singleton
@BeanListener("thermometer")
public class Heater implements TemperatureChangedEventListener {
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
    public void temperatureChanged(TemperatureChangedEvent event) {
        if (event.getCurrentTemperature() < event.getSource().getOptimalTemperatur()) {
            if (enabled == false) {
                System.out.println("Heater started.");
            }
            enabled = true;
        } else {
            if (enabled == true) {
                System.out.println("Heater turned off.");
            }
            enabled = false;
        }

    }
}
