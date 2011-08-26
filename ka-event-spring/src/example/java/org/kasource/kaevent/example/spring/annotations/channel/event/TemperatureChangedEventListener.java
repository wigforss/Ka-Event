package org.kasource.kaevent.example.spring.annotations.channel.event;

import java.util.EventListener;

///CLOVER:OFF
//CHECKSTYLE:OFF
public interface TemperatureChangedEventListener extends EventListener {
	 public void temperatureChanged(TemperatureChangedEvent event);
}
