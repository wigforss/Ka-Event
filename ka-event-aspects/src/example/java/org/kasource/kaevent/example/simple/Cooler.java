package org.kasource.kaevent.example.simple;

import org.kasource.kaevent.example.simple.event.TemperatureChangeEvent;
import org.kasource.kaevent.example.simple.event.TemperatureChangeEventListener;
import org.kasource.kaevent.listener.BeanListener;
import org.kasource.kaevent.listener.RegisterListener;

///CLOVER:OFF
@BeanListener("thermometer")
public class Cooler implements TemperatureChangeEventListener{

	
	private boolean enabled = false;
	
	public Cooler() {
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
		if (event.getCurrentTemperature() > event.getSource().getOptimalTemperatur())
        {
            if(enabled == false) {
            	System.out.println("Cooler started.");
            }
            enabled = true;
        }
        else
        {
            if(enabled == true) {
            	System.out.println("Cooler turned off.");
            }
            enabled =false;
        }

	}

}
