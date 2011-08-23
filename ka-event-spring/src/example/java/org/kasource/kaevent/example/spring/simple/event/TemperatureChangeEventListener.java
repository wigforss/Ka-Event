package org.kasource.kaevent.example.spring.simple.event;

import java.util.EventListener;



public interface TemperatureChangeEventListener extends EventListener {
	 public void temperatureChanged(TemperatureChangeEvent event);
	 
	 
}
