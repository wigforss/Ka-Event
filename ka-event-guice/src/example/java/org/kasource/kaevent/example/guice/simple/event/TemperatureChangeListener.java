package org.kasource.kaevent.example.guice.simple.event;

import java.util.EventListener;

//CHECKSTYLE:OFF
///CLOVER:OFF
public interface TemperatureChangeListener extends EventListener {
	 public void temperatureChanged(TemperatureChangeEvent event);
}