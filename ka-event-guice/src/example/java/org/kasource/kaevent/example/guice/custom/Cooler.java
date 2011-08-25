package org.kasource.kaevent.example.guice.custom;

import org.kasource.kaevent.annotations.listener.BeanListener;
import org.kasource.kaevent.annotations.listener.RegisterListener;
import org.kasource.kaevent.example.guice.custom.event.TemperatureChangeEvent;
import org.kasource.kaevent.example.guice.custom.event.TemperatureChangeEventListener;

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
    public void highTemperature(TemperatureChangeEvent event) {
        if (!enabled) {
            System.out.println("Cooler started.");
        }
        enabled = true;

    }

    @Override
    public void lowTemperature(TemperatureChangeEvent event) {
        if (enabled) {
            System.out.println("Cooler turned off.");
        }
        enabled = false;

    }

    @Override
    public void mediumTemperature(TemperatureChangeEvent event) {
        if (enabled) {
            System.out.println("Cooler turned off.");
        }
        enabled = false;

    }
}
