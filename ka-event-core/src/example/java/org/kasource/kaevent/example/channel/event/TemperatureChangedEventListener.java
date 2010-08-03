package org.kasource.kaevent.example.channel.event;

import java.util.EventListener;



public interface TemperatureChangedEventListener extends EventListener{
	 public void temperatureChanged(TemperatureChangedEvent event);
}
