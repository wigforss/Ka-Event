package org.kasource.kaevent.example.spring.xml.simple.event;

import java.util.EventListener;

///CLOVER:OFF
//CHECKSTYLE:OFF
public interface TemperatureChangeListener extends EventListener {
	 public void temperatureChanged(TemperatureChangeEvent event);
	 
	 
}
