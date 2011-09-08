package org.kasource.kaevent.example.guice.channel.event;

import java.util.EventListener;

//CHECKSTYLE:OFF
///CLOVER:OFF
public interface TemperatureChangedListener extends EventListener {
	 public void temperatureChanged(TemperatureChangedEvent event);
}
