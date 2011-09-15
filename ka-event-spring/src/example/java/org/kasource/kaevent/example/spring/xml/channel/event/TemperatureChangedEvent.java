package org.kasource.kaevent.example.spring.xml.channel.event;

import java.util.EventObject;

import org.kasource.kaevent.annotations.event.Event;
import org.kasource.kaevent.event.BaseEvent;

import org.kasource.kaevent.example.spring.xml.channel.Thermometer;
import org.springframework.context.ApplicationEvent;

///CLOVER:OFF
//CHECKSTYLE:OFF
@Event(listener = TemperatureChangedListener.class)
public class TemperatureChangedEvent extends EventObject {
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
