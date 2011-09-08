package org.kasource.kaevent.example.spring.channel.event;

import java.util.EventListener;

///CLOVER:OFF
//CHECKSTYLE:OFF
public interface TemperatureChangedListener extends EventListener {
	 public void temperatureChanged(TemperatureChangedEvent event);
}
