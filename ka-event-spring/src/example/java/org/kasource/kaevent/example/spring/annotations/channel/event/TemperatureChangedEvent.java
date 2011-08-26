package org.kasource.kaevent.example.spring.annotations.channel.event;

import org.kasource.kaevent.annotations.event.Event;
import org.kasource.kaevent.event.BaseEvent;

import org.kasource.kaevent.example.spring.annotations.channel.Thermometer;

///CLOVER:OFF
//CHECKSTYLE:OFF
@Event(listener = TemperatureChangedEventListener.class, channels = "temperatureChannel", createChannels = true)
public class TemperatureChangedEvent extends BaseEvent {
	private static final long serialVersionUID = 1L;

	private double currentTemperature;
	
	public TemperatureChangedEvent(Thermometer source, double currentTemperature) {
		super(source);
		this.currentTemperature = currentTemperature;
	}
	
	 @Override
	 public Thermometer getSource() {
		 return (Thermometer) super.getSource();
	 }
	 
     public double getCurrentTemperature() {
         return this.currentTemperature;
     }
}
