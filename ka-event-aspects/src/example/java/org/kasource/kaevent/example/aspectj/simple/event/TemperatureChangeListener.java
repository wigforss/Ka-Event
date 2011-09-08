package org.kasource.kaevent.example.aspectj.simple.event;

import java.util.EventListener;



public interface TemperatureChangeListener extends EventListener{
	 public void temperatureChanged(TemperatureChangeEvent event);
}
