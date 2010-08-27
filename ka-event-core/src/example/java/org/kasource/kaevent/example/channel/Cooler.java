package org.kasource.kaevent.example.channel;

import org.kasource.kaevent.example.channel.event.TemperatureChangedEvent;
import org.kasource.kaevent.example.channel.event.TemperatureChangedEventListener;




///CLOVER:OFF
public class Cooler implements TemperatureChangedEventListener{
private boolean enabled = false;
	
	public boolean isEnabled() {
		return enabled;
	}

	

	


	@Override
	public void temperatureChanged(TemperatureChangedEvent event) {
		if (event.getCurrentTemperature() > event.getSource().getOptimalTemperatur())
        {
            if(enabled == false) {
            	System.out.println("Cooler "+event.getSource().getId()+" started.");
            }
            enabled = true;
        }
        else
        {
            if(enabled == true) {
            	System.out.println("Cooler "+event.getSource().getId()+" turned off.");
            }
            enabled =false;
        }
		
	}

}