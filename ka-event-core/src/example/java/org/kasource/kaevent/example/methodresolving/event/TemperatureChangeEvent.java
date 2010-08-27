package org.kasource.kaevent.example.methodresolving.event;

import java.util.EventObject;

import org.kasource.kaevent.event.Event;
import org.kasource.kaevent.event.EventKeyword;
import org.kasource.kaevent.example.methodresolving.Thermometer;


///CLOVER:OFF
@Event(listener=TemperatureChangeEventListener.class)
public class TemperatureChangeEvent extends EventObject{
	private static final long serialVersionUID = 1L;
	private double currentTemperature;
    private TemperatureDirection direction;
    
	 public TemperatureChangeEvent(Thermometer source, double currentTemperature, TemperatureDirection direction)
     {
         super(source);
         this.currentTemperature = currentTemperature;
         this.direction = direction;
     }
	 
	 @Override
	 public Thermometer getSource() {
		 return (Thermometer) super.getSource();
	 }
	 
     public double getCurrentTemperature()
     {
         return this.currentTemperature;
     }
     
     @EventKeyword
     public TemperatureDirection getDirection() {
    	 return direction;
     }

}