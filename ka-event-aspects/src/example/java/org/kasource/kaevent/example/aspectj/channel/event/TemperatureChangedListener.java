package org.kasource.kaevent.example.aspectj.channel.event;

import java.util.EventListener;



public interface TemperatureChangedListener extends EventListener{
	 public void temperatureChanged(TemperatureChangedEvent event);
}
