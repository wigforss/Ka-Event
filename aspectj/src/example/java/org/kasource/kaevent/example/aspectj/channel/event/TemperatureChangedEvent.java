package org.kasource.kaevent.example.aspectj.channel.event;

import org.kasource.kaevent.annotations.event.Event;
import org.kasource.kaevent.event.BaseEvent;
import org.kasource.kaevent.example.aspectj.channel.Thermometer;


///CLOVER:OFF
@Event(listener=TemperatureChangedListener.class, channels="temperatureChannel")
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
