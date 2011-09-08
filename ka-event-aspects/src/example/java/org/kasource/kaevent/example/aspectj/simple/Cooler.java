package org.kasource.kaevent.example.aspectj.simple;

import org.kasource.kaevent.annotations.listener.BeanListener;
import org.kasource.kaevent.annotations.listener.RegisterListener;
import org.kasource.kaevent.example.aspectj.simple.event.TemperatureChangeEvent;
import org.kasource.kaevent.example.aspectj.simple.event.TemperatureChangeListener;

///CLOVER:OFF
@BeanListener("thermometer")
public class Cooler implements TemperatureChangeListener{

	
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
