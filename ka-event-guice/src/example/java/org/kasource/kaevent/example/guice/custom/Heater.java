package org.kasource.kaevent.example.guice.custom;

import org.kasource.kaevent.annotations.listener.BeanListener;
import org.kasource.kaevent.annotations.listener.RegisterListener;
import org.kasource.kaevent.example.guice.custom.event.TemperatureChangeEvent;
import org.kasource.kaevent.example.guice.custom.event.TemperatureChangeEventListener;

import com.google.inject.Singleton;



///CLOVER:OFF
@Singleton
@BeanListener("thermometer")
public class Heater implements TemperatureChangeEventListener{

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
    public void highTemperature(TemperatureChangeEvent event) {
        if(enabled == true) {
            System.out.println("Heater turned off.");
        }
        enabled =false;
        
    }

    
    @Override
    public void lowTemperature(TemperatureChangeEvent event) {
        if(enabled == false) {
            System.out.println("Heater started.");
        }
        enabled = true;
        
    }

    
    @Override
    public void mediumTemperature(TemperatureChangeEvent event) {
        if(enabled == true) {
            System.out.println("Heater turned off.");
        }
        enabled =false;
        
    }

}
