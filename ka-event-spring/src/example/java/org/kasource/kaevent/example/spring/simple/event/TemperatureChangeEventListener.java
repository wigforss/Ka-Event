package org.kasource.kaevent.example.spring.simple.event;

import java.util.EventListener;

///CLOVER:OFF
//CHECKSTYLE:OFF
public interface TemperatureChangeEventListener extends EventListener {
	 public void temperatureChanged(TemperatureChangeEvent event);
	 
	 
}
