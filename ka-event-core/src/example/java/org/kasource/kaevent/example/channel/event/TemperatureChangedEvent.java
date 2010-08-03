package org.kasource.kaevent.example.channel.event;

import org.kasource.kaevent.event.BaseEvent;
import org.kasource.kaevent.event.Event;
import org.kasource.kaevent.example.channel.Thermometer;


@Event(listener=TemperatureChangedEventListener.class)
public class TemperatureChangedEvent extends BaseEvent{
	private static final long serialVersionUID = 1L;

	private double currentTemperature;
	
	public TemperatureChangedEvent(Thermometer source,double currentTemperature) {
		super(source);
		this.currentTemperature = currentTemperature;
	}
	
	 @Override
	 public Thermometer getSource() {
		 return (Thermometer) super.getSource();
	 }
	 
     public double getCurrentTemperature()
     {
         return this.currentTemperature;
     }
}
